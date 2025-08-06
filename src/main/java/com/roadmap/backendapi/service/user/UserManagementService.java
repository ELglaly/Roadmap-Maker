package com.roadmap.backendapi.service.user;

import com.roadmap.backendapi.dto.UserDTO;
import com.roadmap.backendapi.request.user.UserCreateDTO;
import com.roadmap.backendapi.request.user.ChangePasswordRequest;
import com.roadmap.backendapi.request.user.UserUpdateDTO;

/**
 * UserManagementService is an interface that defines methods for managing user accounts.
 * It includes methods for updating user information, registering new users, changing passwords, and deleting users.
 */
public interface UserManagementService {
    UserDTO updateUser(Long id, UserUpdateDTO userUpdateDto);
    UserDTO registerUser(UserCreateDTO request);
    void changePassword(Long userId, ChangePasswordRequest request);
    void deleteUser(Long userId);
}
