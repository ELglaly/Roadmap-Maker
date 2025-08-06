package com.roadmap.backendapi.dto;

import com.roadmap.backendapi.entity.Address;
import com.roadmap.backendapi.entity.PhoneNumber;
import com.roadmap.backendapi.entity.enums.UserRoles;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * Data Transfer Object (DTO) for User.
 * This class is used to transfer data between the application and the client.
 * It contains fields that represent the properties of a User.
 *
 * @see com.roadmap.backendapi.entity.user.User
 */

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class UserDTO implements Serializable {

    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String goal;
    private List<String> interests;
    private List<String> skills;
    private UserRoles role;

    // Embedded UserContact fields
    private String email;
    private PhoneNumber phone;
    private Address address;
}