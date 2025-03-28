package com.roadmap.backendapi.service.user;

import com.roadmap.backendapi.dto.UserDTO;
import com.roadmap.backendapi.request.user.RegistrationRequest;
import com.roadmap.backendapi.request.user.UpdateUserRequest;
import com.roadmap.backendapi.request.user.changePasswordRequest;

public interface UserManagementService {
    UserDTO updateUser(Long id, UpdateUserRequest request);
    UserDTO registerUser(RegistrationRequest request);
    void changePassword(Long userId, changePasswordRequest request);
    void deleteUser(Long userId);
}
