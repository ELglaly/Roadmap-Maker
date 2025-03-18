
package com.roadmap.backendapi.mapper;

import com.roadmap.backendapi.dto.UserDTO;
import com.roadmap.backendapi.model.User;


public interface IUserMapper {
    UserDTO toDTO(User user);
    User toEntity(UserDTO userDTO);
}