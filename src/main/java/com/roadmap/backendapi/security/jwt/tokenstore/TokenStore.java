package com.roadmap.backendapi.security.jwt.tokenstore;

/**
 * TokenStore interface defines methods for managing JWT tokens.
 * It provides methods to save, retrieve, check existence, and remove tokens.
 */
public interface TokenStore {
    void saveToken(String token, String username);

    String getToken(String username);

    boolean isUserLoggedIn(String username);

    void removeToken(String username);

    boolean isTokenPresent(String token);
}
