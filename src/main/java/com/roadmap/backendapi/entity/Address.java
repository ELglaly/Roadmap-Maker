package com.roadmap.backendapi.entity;

import com.roadmap.backendapi.Consts;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;


/**
 * Entity class representing an Address.
 * This class is used to map the Address table in the database.
 * It contains fields that represent the properties of an Address.
 *
 * @see User
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
    @Pattern(regexp = Consts.RegularExpression.STRING_PATTERN, message = Consts.RegularExpression.STRING_PATTERN_ERROR)
    private String city;

    @Column(length = 15, nullable = false)
    @NotBlank
    @Pattern(regexp = Consts.RegularExpression.ZIB_PATTERN, message = Consts.RegularExpression.ZIB_PATTERN_ERROR)
    private String zip;

    @Column(length = 50, nullable = false)
    @NotBlank
    @Pattern(regexp = Consts.RegularExpression.STRING_PATTERN, message = Consts.RegularExpression.STRING_PATTERN_ERROR)
    private String country;

}
