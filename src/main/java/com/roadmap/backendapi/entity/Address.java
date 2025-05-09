package com.roadmap.backendapi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

/**
 * Entity class representing an Address.
 * This class is used to map the Address table in the database.
 * It contains fields that represent the properties of an Address.
 *
 * @see com.roadmap.backendapi.entity.User
 */
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Address {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String street;
    @Column(length = 20, nullable = false)
    private String city;
    @Column(length = 5, nullable = false)
    private String zip;
    @Column(length = 50, nullable = false)
    private String country;
}
