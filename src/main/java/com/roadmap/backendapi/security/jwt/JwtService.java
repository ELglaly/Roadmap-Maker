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
import java.util.concurrent.ConcurrentHashMap;

@Service
public class JwtService {
    // Add JWT service implementation here
    private static String secretKey="";
    private static final long validityInMilliseconds = 60*60*1000; // 1h
    private static final long logoutTimeInMilliseconds = 60*60*1000; // 1m
    private final TokenStore tokenStore;
    // implement the jwt service here


    public JwtService(TokenStore tokenStore) {
        this.tokenStore = tokenStore;
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
            SecretKey sk= keyGenerator.generateKey();
            secretKey = Base64.getEncoder().encodeToString(sk.getEncoded());

        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }


    public String generateToken(String username)  throws TokenGenerationException {
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
                String token = Jwts.builder()
                        .claims(claims)
                        .subject(username)
                        .issuedAt(Date.from(now))
                        .expiration(Date.from(now.plusMillis(validityInMilliseconds)))
                        .signWith(getKey())
                        .compact();

                // 4. Persist token
                tokenStore.saveToken(token, username);
                return token;

            } catch (JwtException e) {
                throw new TokenGenerationException("Failed to generate token");
            }
        }
    private  SecretKey getKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }
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

    public boolean validateToken(String token, UserDetails userDetails) {
        return userDetails.getUsername().equals(getUsername(token))&&
                tokenStore.getToken(userDetails.getUsername()).equals(token);
    }

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
    public String refreshToken(String token) {
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
    public void handleTokenRefresh(HttpServletResponse response, String jwt) {
        // implement the method here
        response.setHeader("Authorization", "Bearer " + refreshToken(jwt));
    }
}
