
package com.roadmap.backendapi.request.user;

import com.roadmap.backendapi.dto.AddressDTO;
import com.roadmap.backendapi.dto.PhoneNumberDTO;
import com.roadmap.backendapi.entity.enums.UserRoles;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateDTO {

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


    @Size(min = 8, max = 128, message = "Password must be 8-128 characters")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Password must contain lowercase, uppercase, digit, and special character")
    private String passwordHash;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Size(max = 255, message = "Email too long")
    private String email;

}
