package com.roadmap.backendapi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@Builder
@NoArgsConstructor
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
