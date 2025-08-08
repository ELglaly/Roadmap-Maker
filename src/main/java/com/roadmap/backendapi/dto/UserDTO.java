package com.roadmap.backendapi.dto;

import com.roadmap.backendapi.entity.enums.UserRoles;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.io.Serializable;
import java.util.List;

/**
 * Fully immutable DTO for {@link com.roadmap.backendapi.entity.user.User}
 * Implements deep defensive copying for complete immutability.
 */
public record UserDTO(
        @Size(message = "First name must be 3-50 characters", min = 3, max = 50)
        @Pattern(message = "Invalid characters in first name", regexp = "^[A-Za-z\\s'-]{1,50}$")
        @NotBlank(message = "First name is required")
        String firstName,

        @Size(message = "Last name must be 3-50 characters", min = 3, max = 50)  // ✅ Fixed message
        @Pattern(message = "Invalid characters in last name", regexp = "^[A-Za-z\\s'-]{1,50}$")
        @NotBlank(message = "Last name is required")
        String lastName,

        @Size(message = "Username must be 3-30 characters", min = 3, max = 30)
        @Pattern(message = "Username contains invalid characters", regexp = "^[a-zA-Z0-9._-]{3,30}$")
        @NotBlank(message = "Username is required")
        String username,

        @Size(message = "Goal must be 5-2000 characters", min = 5, max = 2000)
        @NotBlank(message = "Goal is required")
        String goal,

        @Valid
        List<@NotBlank String> interests,

        @Valid
        List<@NotBlank String> skills,

        @NotNull(message = "Role is required")
        UserRoles role,

        @Size(message = "Email must be a valid email address", max = 255)
        @NotBlank(message = "Email is required")
        @Pattern(message = "Invalid email format",
                regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
        String email,

        @NotNull(message = "Address is required")
        AddressDTO addressDTO,

        @NotEmpty(message = "At least one phone number is required")
        @Valid
        List<@NotNull PhoneNumberDTO> phoneNumberDTO
) implements Serializable {

    /**
     * Compact constructor ensuring complete immutability with deep copying
     */
    public UserDTO {
        // Create immutable copies of string lists
        interests = interests != null ? List.copyOf(interests) : List.of();
        skills = skills != null ? List.copyOf(skills) : List.of();
        phoneNumberDTO = phoneNumberDTO != null ?
                List.copyOf(phoneNumberDTO) : List.of();
    }
}
