package com.roadmap.backendapi.security.jwt.tokenstore;

public interface TokenStore {
    void saveToken(String token, String username);

    String getToken(String username);

    boolean isUserLoggedIn(String username);

    void removeToken(String username);

    boolean isTokenPresent(String token);
}
