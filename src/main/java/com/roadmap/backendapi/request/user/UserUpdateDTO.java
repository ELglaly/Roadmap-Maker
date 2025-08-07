package com.roadmap.backendapi.request.user;

import com.roadmap.backendapi.dto.AddressDTO;
import com.roadmap.backendapi.dto.PhoneNumberDTO;
import com.roadmap.backendapi.entity.enums.UserRoles;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO for updating user information.
 * This class is used to transfer user data during update operations.
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDTO implements Serializable {

    @NotBlank(message = "First name is required")
    @Size(min = 3, max = 50, message = "First name must be 3-50 characters")
    @Pattern(regexp = "^[A-Za-z\\s'-]{1,50}$", message = "Invalid characters in first name")
    private String firstName;

    @NotBlank(message = "First name is required")
    @Size(min = 3, max = 50, message = "First name must be 3-50 characters")
    @Pattern(regexp = "^[A-Za-z\\s'-]{1,50}$", message = "Invalid characters in first name")
    private String lastName;

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 30, message = "Username must be 3-30 characters")
    @Pattern(regexp = "^[a-zA-Z0-9._-]{3,30}$", message = "Username contains invalid characters")
    private String username;

    @NotBlank(message = "Goal is required")
    @Size(min = 5, max = 2000, message = "Goal must be 5-2000 characters")
    private String goal;

    @Builder.Default
    private List<String> interests =new ArrayList<>();

    @Builder.Default
    private List<String> skills = new ArrayList<>();


    @Enumerated(EnumType.STRING)
    @Builder.Default
    private UserRoles role = UserRoles.USER;

    @Size(min = 8, max = 128, message = "Password must be 8-128 characters")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Password must contain lowercase, uppercase, digit, and special character")
    private String passwordHash;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Size(max = 255, message = "Email too long")
    private String email;


    private AddressDTO addressDto;


    private List<PhoneNumberDTO> phoneNumberDto;


}
