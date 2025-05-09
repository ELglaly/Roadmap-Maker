package com.roadmap.backendapi.request.user;

import com.roadmap.backendapi.entity.Address;
import com.roadmap.backendapi.entity.PhoneNumber;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequest {

    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstName;

    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String lastName;

    @Email(message = "Invalid email format")
    private String email;

    @Size(min = 5, max = 20, message = "Username must be between 5 and 20 characters")
    private String username;

    @Size(max = 500, message = "Goal cannot exceed 500 characters")
    private String goal;

    @Size(max = 500, message = "Interests cannot exceed 500 characters")
    private String interests;

    @Size(max = 500, message = "Skills cannot exceed 500 characters")
    private String skills;

    private AddressRequest address;

    private PhoneNumber phoneNumber;

}
