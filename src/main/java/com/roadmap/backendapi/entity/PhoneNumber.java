package com.roadmap.backendapi.entity;

import jakarta.persistence.*;
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
    private String countryCode;
    @Column(length = 10, nullable = false)
    private String number;

    @OneToOne
    private User user;

}
