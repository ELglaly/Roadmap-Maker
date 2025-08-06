package com.roadmap.backendapi.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * DTO for {@link com.roadmap.backendapi.entity.PhoneNumber}
 */
public record PhoneNumberDTO(
        @Size(message = "Country code must be 1-5 characters", min = 1, max = 5) @Pattern(message = "Invalid country code format", regexp = "\\+?[1-9]\\d{0,3}") @NotBlank(message = "Country code is required") String countryCode,
        @NotNull(message = "Phone number is required") @Size(message = "Phone number must be 4-15 digits", min = 4, max = 15) @Pattern(message = "Phone number must contain digits only", regexp = "\\d+") @NotEmpty @NotBlank(message = "Phone number is required") String number) implements Serializable {


}