package com.roadmap.backendapi.service.user;

import com.roadmap.backendapi.dto.UserDTO;
import com.roadmap.backendapi.request.user.LoginRequest;

public interface UserSearchService {
    UserDTO getUserById(Long userId);
    String loginUser(LoginRequest loginRequest);
    UserDTO getUserByEmail(String email);
    UserDTO getUserByUsername(String username);
}
