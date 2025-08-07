package com.roadmap.backendapi.entity;

import com.roadmap.backendapi.entity.enums.PhoneNumberType;
import com.roadmap.backendapi.entity.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * Entity class representing a PhoneNumber.
 * This class is used to map the PhoneNumber table in the database.
 * It contains fields that represent the properties of a PhoneNumber.
 *
 * @see com.roadmap.backendapi.entity.User
 */
@Entity
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Setter
@Getter
public class PhoneNumber {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @Column(length = 5, nullable = false)
    @NotBlank(message = "Country code is required")
    @Size(min = 1, max = 5, message = "Country code must be 1-5 characters")
    @Pattern(regexp = "\\+?[1-9]\\d{0,3}", message = "Invalid country code format")
    private String countryCode;

    @Column(length = 15, nullable = false)
    @NotBlank(message = "Phone number is required")
    @Size(min = 4, max = 15, message = "Phone number must be 4-15 digits")
    @Pattern(regexp = "\\d+", message = "Phone number must contain digits only")
    private String number;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotBlank(message = "Phone type is required")
    private PhoneNumberType phoneType;


    @ManyToOne()
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
