package com.roadmap.backendapi.service.user;

import com.roadmap.backendapi.dto.UserDTO;
import com.roadmap.backendapi.request.user.CreateUserRequest;
import com.roadmap.backendapi.request.user.UpdateUserRequest;

public interface UserManagementService {
    UserDTO updateUser(UpdateUserRequest request);
    UserDTO createUser(CreateUserRequest request);
    void deleteUser(Long userId);
}
