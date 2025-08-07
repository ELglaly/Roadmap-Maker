package com.roadmap.backendapi.entity.user;

import com.roadmap.backendapi.entity.Address;
import com.roadmap.backendapi.entity.PhoneNumber;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Embeddable
public class UserContact {

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Size(max = 255, message = "Email too long")
    @Column(unique = true, nullable = false, length = 255)
    private String email;

    @OneToMany(mappedBy = "user" ,cascade = CascadeType.ALL,orphanRemoval = true,fetch = FetchType.LAZY)
    private List<PhoneNumber> phoneNumber;


    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Address address;


}
