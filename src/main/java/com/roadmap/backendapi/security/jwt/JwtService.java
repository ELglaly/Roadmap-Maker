package com.roadmap.backendapi.security.jwt;

import com.roadmap.backendapi.exception.user.InvalidTokenException;
import com.roadmap.backendapi.exception.user.TokenGenerationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;


/** * JwtService is a service class that handles JWT token generation, validation,
 * and user authentication. It provides methods to create tokens, extract claims,
 * and check token validity.
 *
 * @see com.roadmap.backendapi.security.jwt.JwtAuthenticationFilter
 * @see com.roadmap.backendapi.security.jwt.JwtService
 */
@Service
public class JwtService {
    // If using Redis or another distributed cache:
    private final RedisTemplate<String, Date> redisTemplate;


    private final String secretKey;

    @Value("${jwt.expiration.ms}")
    private long expirationMs;

    @Value("${jwt.logout-time.ms}")
    private long logoutTimeMs;


    public JwtService(RedisTemplate<String, Date> redisTemplate) {
        this.redisTemplate = redisTemplate;
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
            SecretKey sk = keyGenerator.generateKey();
            secretKey = Base64.getEncoder().encodeToString(sk.getEncoded());

        } catch (NoSuchAlgorithmException e) {
            throw new TokenGenerationException("Failed to generate secret key" +e.getMessage());
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

        try {
            // 3. Build token
            Instant now = Instant.now();
            return Jwts.builder()
                    .subject(username)
                    .issuedAt(Date.from(now))
                    .expiration(Date.from(now.plusMillis(expirationMs)))
                    .signWith(getKey())
                    .compact();

        } catch (JwtException ex) {
            throw new TokenGenerationException("Failed to generate token");
        }
    }

    /**
     * This method retrieves the secret key used for signing the JWT.
     * It uses the HmacSHA256 algorithm to generate a key from the secretKey string.
     * @return The SecretKey object used for signing the JWT.
     */
    private SecretKey getKey() {
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
            String username = parseClaims(token).getSubject();
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
        return userDetails.getUsername().equals(getUsername(token))
                && !isTokenExpired(token)
                && !isTokenBlacklisted(token)
                ;
    }


    /**
     * This method checks if the token is expired.
     * It compares the current time with the expiration time of the token.
     * @param token The JWT token to be checked.
     * @return true if the token is expired, false otherwise.
     */
    public boolean isTokenExpired(String token) {
        try {
            return parseClaims(token)
                    .getExpiration()
                    .before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }


    public boolean isTokenBlacklisted(String token) {
        if (token == null || token.trim().isEmpty()) {
            return false;
        }
        long expirationTime = Objects.requireNonNull(redisTemplate.opsForValue().get(token)).getTime();
        return expirationTime > System.currentTimeMillis();
    }

    public void blacklistToken(String token) {
        if (token == null || token.trim().isEmpty()) {
            throw new SecurityException("Token cannot be null or empty");
        }
        else if (isTokenBlacklisted(token)) {
            throw new SecurityException("Token is already blacklisted");
        }

        Date expirationDate = new Date(System.currentTimeMillis() + logoutTimeMs);
        redisTemplate.opsForValue().set(
                token,
                expirationDate,
                logoutTimeMs,
                TimeUnit.MILLISECONDS
        );
    }

    private Claims parseClaims(String token) {
        return  Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

}
