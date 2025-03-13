package com.roadmap.backendapi.dto;

import com.roadmap.backendapi.model.Address;
import com.roadmap.backendapi.model.PhoneNumber;
import com.roadmap.backendapi.model.Progress;
import com.roadmap.backendapi.model.Roadmap;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Builder
@EqualsAndHashCode
@Data
public class UserDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String password;
    private String goal;
    private String interests;
    private String skills ;
    private Address address;
    private PhoneNumber phoneNumber;
    private List<RoadmapDTO> roadmapsDTO;
    private List<ProgressDTO> progressDTO;
}
