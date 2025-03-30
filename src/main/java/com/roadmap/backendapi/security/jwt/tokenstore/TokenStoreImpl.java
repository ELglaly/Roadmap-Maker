package com.roadmap.backendapi.security.jwt.tokenstore;

import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class TokenStoreImpl implements TokenStore {
    // Add TokenStore implementation here
    private final ConcurrentHashMap<String, String> tokenStore = new ConcurrentHashMap<>();

    public void saveToken(String token, String username) {
        tokenStore.put(username, token);
    }

    public String getToken(String username) {
        return tokenStore.get(username);
    }

    public boolean isUserLoggedIn(String username) {
        return tokenStore.containsKey(username);
    }

    public void removeToken(String username) {
        tokenStore.remove(username);
    }

    @Override
    public boolean isTokenPresent(String token) {
        return tokenStore.containsValue(token);
    }
}
