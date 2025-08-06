package com.roadmap.backendapi.response.user;

import com.roadmap.backendapi.entity.enums.UserRoles;
import lombok.Value;

import java.util.List;

@Value
public class UserResponseDTO {
     Long id;
     String firstName;
     String lastName;
     String username;
     String goal;
     List<String> interests;
     List<String> skills;
     UserRoles role;
     String email;
     String phone;
}
