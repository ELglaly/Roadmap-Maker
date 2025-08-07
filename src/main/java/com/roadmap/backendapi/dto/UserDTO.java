package com.roadmap.backendapi.dto;

import com.roadmap.backendapi.entity.enums.UserRoles;
import com.roadmap.backendapi.entity.user.UserContact;
import com.roadmap.backendapi.entity.user.UserSecurity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link com.roadmap.backendapi.entity.user.User}
 */
public record UserDTO(
        @Size(message = "First name must be 3-50 characters", min = 3, max = 50) @Pattern(message = "Invalid characters in first name", regexp = "^[A-Za-z\\s'-]{1,50}$") @NotBlank(message = "First name is required") String firstName,
        @Size(message = "First name must be 3-50 characters", min = 3, max = 50) @Pattern(message = "Invalid characters in first name", regexp = "^[A-Za-z\\s'-]{1,50}$") @NotBlank(message = "First name is required") String lastName,
        @Size(message = "Username must be 3-30 characters", min = 3, max = 30) @Pattern(message = "Username contains invalid characters", regexp = "^[a-zA-Z0-9._-]{3,30}$") @NotBlank(message = "Username is required") String username,
        @Size(message = "Goal must be 5-2000 characters", min = 5, max = 2000) @NotBlank(message = "Goal is required") String goal,
        List<String> interests, List<String> skills, UserRoles role,
        @Size(message = "Email must be a valid email address", max = 255) @NotBlank(message = "Email is required") @Pattern(message = "Invalid email format", regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z]{2,}$") String email,
        @NotNull AddressDTO addressDTO,
        @NotNull List<PhoneNumberDTO> phoneNumberDTO) implements Serializable {
}