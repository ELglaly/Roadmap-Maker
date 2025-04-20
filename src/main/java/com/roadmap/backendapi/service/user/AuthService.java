package com.roadmap.backendapi.service.user;

import com.roadmap.backendapi.request.user.LoginRequest;

public interface AuthService {
    String loginUser(LoginRequest loginRequest);
    void logoutUser(String token);
}
