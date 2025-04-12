package com.roadmap.backendapi.service.user;

import com.roadmap.backendapi.dto.UserDTO;
import com.roadmap.backendapi.request.user.RegistrationRequest;
import com.roadmap.backendapi.request.user.UpdateUserRequest;
import com.roadmap.backendapi.request.user.changePasswordRequest;

/**
 * UserManagementService is an interface that defines methods for managing user accounts.
 * It includes methods for updating user information, registering new users, changing passwords, and deleting users.
 */
public interface UserManagementService {
    UserDTO updateUser(Long id, UpdateUserRequest request);
    UserDTO registerUser(RegistrationRequest request);
    void changePassword(Long userId, changePasswordRequest request);
    void deleteUser(Long userId);
}
