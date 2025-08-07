package com.roadmap.backendapi.dto;

import com.roadmap.backendapi.entity.enums.AddressType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.io.Serializable;

/**
 * DTO for {@link com.roadmap.backendapi.entity.Address}
 */
public record AddressDTO(@NotBlank(message = "Street is required") String street,
                         @Pattern(message = "must be 2-50 characters, letters, spaces, hyphens and apostrophes only", regexp = "^[A-Za-z\\s'-]{2,50}$") @NotBlank(message = "City is required") String city,
                         @Pattern(message = "ZIP code must be 3-15 characters, alphanumeric with spaces and hyphens", regexp = "^[A-Z0-9\\s-]{3,15}$") @NotBlank(message = "Zip code is required") String zip,
                         @Pattern(message = "must be 2-50 characters, letters, spaces, hyphens and apostrophes only", regexp = "^[A-Za-z\\s'-]{2,50}$") @NotBlank(message = "Country is required") String country,
                         @NotNull AddressType addressType) implements Serializable {
}