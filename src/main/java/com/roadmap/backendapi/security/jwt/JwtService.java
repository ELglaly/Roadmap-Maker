package com.roadmap.backendapi.security.jwt;

import com.roadmap.backendapi.entity.User;
import com.roadmap.backendapi.exception.user.AlreadyLoggedInException;
import com.roadmap.backendapi.exception.user.InvalidTokenException;
import com.roadmap.backendapi.exception.user.TokenGenerationException;
import com.roadmap.backendapi.security.jwt.tokenstore.TokenStore;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.InvalidKeyException;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/** * JwtService is a service class that handles JWT token generation, validation,
 * and user authentication. It provides methods to create tokens, extract claims,
 * and check token validity.
 *
 * @see com.roadmap.backendapi.security.jwt.JwtAuthenticationFilter
 * @see com.roadmap.backendapi.security.jwt.tokenstore.TokenStore
 */
@Service
public class JwtService {

    private static String secretKey = "";
    private static final long validityInMilliseconds = 60 * 60 * 1000; // 1h
    private static final long logoutTimeInMilliseconds = 60 * 60 * 1000; // 1m

    public JwtService() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
            SecretKey sk = keyGenerator.generateKey();
            secretKey = Base64.getEncoder().encodeToString(sk.getEncoded());

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * This method generates a JWT token for the given username.
     * It includes claims such as username and expiration time.
     * @param username The username for which to generate the token.
     * @return The generated JWT token.
     * @throws TokenGenerationException if token generation fails.
     */
    public String generateToken(String username) throws TokenGenerationException {
        // 1. Validate input
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be null or blank");
        }
        if (validityInMilliseconds <= 0) {
            throw new IllegalArgumentException("Token validity must be positive");
        }

        // 2. Prepare claims
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", username);

        try {
            // 3. Build token
            Instant now = Instant.now();
            return Jwts.builder()
                    .claims(claims)
                    .subject(username)
                    .issuedAt(Date.from(now))
                    .expiration(Date.from(now.plusMillis(validityInMilliseconds)))
                    .signWith(getKey())
                    .compact();

        } catch (JwtException e) {
            throw new TokenGenerationException("Failed to generate token");
        }
    }

    /**
     * This method retrieves the secret key used for signing the JWT.
     * It uses the HmacSHA256 algorithm to generate a key from the secretKey string.
     * @return The SecretKey object used for signing the JWT.
     */
    private  SecretKey getKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    /**
     * This method retrieves the username from the token.
     * It parses the token and extracts the username claim.
     * @param token The JWT token from which to extract the username.
     * @return The username extracted from the token.
     * @throws InvalidTokenException if the token is invalid or missing claims.
     */
    public String getUsername(String token) throws InvalidTokenException {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(getKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            String username = claims.get("username", String.class);
            if (username == null) {
                throw new InvalidTokenException("Missing username claim");
            }
            return username;

        } catch (JwtException | IllegalArgumentException e) {
            throw new InvalidTokenException("Token validation failed");
        }
    }

    /**
     * This method checks if the token is valid.
     * It compares the username in the token with the username in the UserDetails object.
     * @param token The JWT token to be checked.
     * @param userDetails The UserDetails object containing user information.
     * @return true if the token is valid, false otherwise.
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        return userDetails.getUsername().equals(getUsername(token));
    }


    /**
     * This method checks if the token is expired.
     * It compares the current time with the expiration time of the token.
     * @param token The JWT token to be checked.
     * @return true if the token is expired, false otherwise.
     */
    public boolean isTokenExpired(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getExpiration()
                    .before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * This method checks if the token is within the logout time.
     * It compares the current time with the expiration time of the token.
     * @param token The JWT token to be checked.
     * @return true if the token is within the logout time, false otherwise.
     */
    public boolean isWithinLogoutTime(String token) {
        try {
            Date expiration = Jwts.parser()
                    .verifyWith(getKey())
                    .build().parseSignedClaims(token)
                    .getPayload()
                    .getExpiration();
            return new Date().before(new Date(expiration.getTime() + logoutTimeInMilliseconds));
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * This method is used to handle the token refresh process.
     * It checks if the token is expired and generates a new token if necessary.
     * @param response The HTTP response object.
     * @param jwt The JWT token to be refreshed.
     */
    public void handleTokenRefresh(HttpServletResponse response, String jwt) {
        // implement the method here
        response.setHeader("Authorization", "Bearer " + refreshToken(jwt));
    }


    /**
     * This method is used to refresh the token if it is expired.
     * It generates a new token with the same claims and a new expiration time.
     * @param token The expired token to be refreshed.
     * @return The new token if the old one is expired, otherwise an empty string.
     */
    private String refreshToken(String token) {
        // implement the method here
        if (isTokenExpired(token)) {
            try {
                return Jwts.builder()
                        .claims(Jwts.parser()
                                .verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                                .build()
                                .parseSignedClaims(token)
                                .getPayload())
                        .issuedAt(new Date(System.currentTimeMillis()))
                        .expiration(new Date(System.currentTimeMillis() + validityInMilliseconds))
                        .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                        .compact();
            } catch (JwtException | IllegalArgumentException e) {
                return "";
            }
        }
        return "";
    }


}
