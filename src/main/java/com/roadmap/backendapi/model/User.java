package com.roadmap.backendapi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Entity
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String firstName;
    @Column
    private String lastName;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(unique = true, nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;

    @Column(nullable = false,columnDefinition = "TEXT")
    private String goal;
    @Column(nullable = false,columnDefinition = "TEXT")
    private String interests;
    @Column(nullable = false,columnDefinition = "TEXT")
    private String skills ;

    @OneToOne(cascade = CascadeType.ALL,orphanRemoval = true)
    private Address address;

    @OneToOne(cascade = CascadeType.ALL,orphanRemoval = true)
    private PhoneNumber phoneNumber;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Roadmap> roadmaps;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Progress> progress;

}
