package com.roadmap.backendapi.service.user;

import com.roadmap.backendapi.dto.UserDTO;

public interface UserSearchService {
    UserDTO getUserById(Long userId);
    UserDTO getUserByEmail(String email);
    UserDTO getUserByUsername(String username);
}
