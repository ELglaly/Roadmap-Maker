package com.roadmap.backendapi.dto;

import com.roadmap.backendapi.Consts;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.io.Serializable;

/**
 * DTO for {@link com.roadmap.backendapi.entity.Address}
 */
public record AddressDTO(@Column(nullable = false) @NotBlank String street,
                         @Column(length = 50, nullable = false) @NotBlank @Pattern(regexp = Consts.RegularExpression.STRING_PATTERN, message = Consts.RegularExpression.STRING_PATTERN_ERROR) String city,
                         @Column(length = 15, nullable = false) @NotBlank @Pattern(regexp = Consts.RegularExpression.ZIB_PATTERN, message = Consts.RegularExpression.ZIB_PATTERN_ERROR) String zip,
                         @Column(length = 50, nullable = false) @NotBlank @Pattern(regexp = Consts.RegularExpression.STRING_PATTERN, message = Consts.RegularExpression.STRING_PATTERN_ERROR) String country) implements Serializable {

}