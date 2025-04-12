package com.roadmap.backendapi.dto;

import com.roadmap.backendapi.entity.Address;
import com.roadmap.backendapi.entity.PhoneNumber;
import lombok.*;

import java.util.List;

/**
 * Data Transfer Object (DTO) for User.
 * This class is used to transfer data between the application and the client.
 * It contains fields that represent the properties of a User.
 *
 * @see com.roadmap.backendapi.entity.User
 */

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class UserDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String password;
    private String role;
    private boolean enabled;
    private String goal;
    private String interests;
    private String skills ;
    private Address address;
    private PhoneNumber phoneNumber;
    private List<RoadmapDTO> roadmapsDTO;
    private List<ProgressDTO> progressDTO;
}
