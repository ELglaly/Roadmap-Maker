package com.roadmap.backendapi.entity;

import com.roadmap.backendapi.utils.Const;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;


/**
 * Entity class representing an Address.
 * This class is used to map the Address table in the database.
 * It contains fields that represent the properties of an Address.
 *
 * @see com.roadmap.backendapi.dto.AddressDTO
 */
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank
    private String street;

    @Column(length = 50, nullable = false)
    @NotBlank
    @Pattern(regexp = Const.RegularExpressions.STRING_PATTERN, message = Const.RegularExpressions.STRING_PATTERN_ERROR)
    private String city;

    @Column(length = 15, nullable = false)
    @NotBlank
    @Pattern(regexp = Const.RegularExpressions.ZIP_PATTERN, message = Const.RegularExpressions.ZIP_PATTERN_ERROR)
    private String zip;

    @Column(length = 50, nullable = false)
    @NotBlank
    @Pattern(regexp = Const.RegularExpressions.STRING_PATTERN, message = Const.RegularExpressions.STRING_PATTERN_ERROR)
    private String country;

}
