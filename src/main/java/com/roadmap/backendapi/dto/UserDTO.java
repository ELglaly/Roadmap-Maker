package com.roadmap.backendapi.dto;

import com.roadmap.backendapi.entity.Address;
import com.roadmap.backendapi.entity.PhoneNumber;
import lombok.*;

import java.util.List;

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
