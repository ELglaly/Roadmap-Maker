package com.roadmap.backendapi.service.user;

import com.roadmap.backendapi.dto.UserDTO;

/**
 * UserSearchService is an interface that defines methods for searching and retrieving user information.
 * It includes methods for getting user details by ID, email, and username, as well as handling user login and logout.
 */
public interface UserSearchService {
    UserDTO getUserById(Long userId);
    UserDTO getUserByEmail(String email);
    UserDTO getUserByUsername(String username);
}
