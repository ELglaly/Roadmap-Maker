package com.roadmap.backendapi.entity;

import com.roadmap.backendapi.entity.enums.UserRoles;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

/**
 * Entity class representing a User.
 * This class is used to map the User table in the database.
 * It contains fields that represent the properties of a User.
 *
 * @see com.roadmap.backendapi.entity.Progress
 * @see com.roadmap.backendapi.entity.Roadmap
 */
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false,columnDefinition = "ENUM('ADMIN','USER','GUEST')")
    private UserRoles role;

    @Column(nullable = false)
    private boolean enabled;

    @OneToOne(cascade = CascadeType.ALL,orphanRemoval = true)
    private Address address;

    @OneToOne(cascade = CascadeType.ALL,orphanRemoval = true)
    private PhoneNumber phoneNumber;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Roadmap> roadmaps;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Progress> progress;

}
