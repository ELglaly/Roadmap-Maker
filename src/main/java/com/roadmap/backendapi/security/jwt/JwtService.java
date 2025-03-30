package com.roadmap.backendapi.security.jwt;

import com.roadmap.backendapi.entity.User;
import com.roadmap.backendapi.exception.user.AlreadyLoggedInException;
import com.roadmap.backendapi.security.UserDetails;
import com.roadmap.backendapi.security.jwt.tokenstore.TokenStore;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
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
    private static final long logoutTimeInMilliseconds = 60*1000; // 1m
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


    public String generateToken(String username) {

        if (tokenStore.isUserLoggedIn(username)) {
            throw new AlreadyLoggedInException("User is already logged in");
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put("username", username);
        String token= Jwts.builder()
                .claims(claims)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + validityInMilliseconds))
                .signWith(io.jsonwebtoken.security.Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
        tokenStore.saveToken(token, username);
        return token;
    }
    public String getUsername(String token) {
        // implement the method here
        return Jwts.parser()
                .verifyWith(io.jsonwebtoken.security.Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("username", String.class);
    }

    public String getEmail(String token) {
        // implement the method here

        return Jwts.parser()
                .verifyWith(io.jsonwebtoken.security.Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("email", String.class);
    }

    public String getRole(String token) {
        // implement the method here

        return Jwts.parser()
                .verifyWith(io.jsonwebtoken.security.Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("role", String.class);
    }
    public Long getId(String token) {
        // implement the method here

        return Jwts.parser()
                .verifyWith(io.jsonwebtoken.security.Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("id", Long.class);
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        // implement the method here
        return userDetails.getUsername().equals(getUsername(token))&& tokenStore.getToken(userDetails.getUsername()).equals(token);
    }

    public boolean isTokenExpired(String token) {
        return Jwts.parser()
                .verifyWith(io.jsonwebtoken.security.Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration()
                .before(new Date());
    }
    public boolean isWithinLogoutTime(String token) {
        Date expiration = Jwts.parser()
                .verifyWith(io.jsonwebtoken.security.Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build().parseSignedClaims(token)
                .getPayload()
                .getExpiration();
        return new Date().before(new Date(expiration.getTime() + logoutTimeInMilliseconds));
    }
    public String refreshToken(String token) {
        // implement the method here
        if (isTokenExpired(token)) {
            return Jwts.builder()
                    .claims(Jwts.parser()
                            .verifyWith(io.jsonwebtoken.security.Keys.hmacShaKeyFor(secretKey.getBytes()))
                            .build()
                            .parseSignedClaims(token)
                            .getPayload())
                    .issuedAt(new Date(System.currentTimeMillis()))
                    .expiration(new Date(System.currentTimeMillis() + validityInMilliseconds))
                    .signWith(io.jsonwebtoken.security.Keys.hmacShaKeyFor(secretKey.getBytes()))
                    .compact();
        }
        return null;
    }


    public void handleTokenRefresh(HttpServletResponse response, String jwt) {
        // implement the method here
        response.setHeader("Authorization", "Bearer " + refreshToken(jwt));
    }
}
