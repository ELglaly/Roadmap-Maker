package com.roadmap.backendapi.entity.user;

import com.roadmap.backendapi.entity.Roadmap;
import com.roadmap.backendapi.entity.enums.UserRoles;
import com.roadmap.backendapi.entity.user.UserContact;
import com.roadmap.backendapi.entity.user.UserSecurity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
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
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "First name is required")
    @Size(min = 3, max = 50, message = "First name must be 3-50 characters")
    @Pattern(regexp = "^[A-Za-z\\s'-]{1,50}$", message = "Invalid characters in first name")
    private String firstName;

    @NotBlank(message = "First name is required")
    @Size(min = 3, max = 50, message = "First name must be 3-50 characters")
    @Pattern(regexp = "^[A-Za-z\\s'-]{1,50}$", message = "Invalid characters in first name")
    private String lastName;

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 30, message = "Username must be 3-30 characters")
    @Pattern(regexp = "^[a-zA-Z0-9._-]{3,30}$", message = "Username contains invalid characters")
    @Column(unique = true, nullable = false, length = 30)
    private String username;

    @NotBlank(message = "Goal is required")
    @Size(min = 5, max = 2000, message = "Goal must be 5-2000 characters")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String goal;

    @Column(nullable = false)
    @ElementCollection
    @Builder.Default
    private List<String> interests =new ArrayList<>();

    @Column(nullable = false)
    @ElementCollection
    @Builder.Default
    private List<String> skills = new ArrayList<>();

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,orphanRemoval = true)
    @Builder.Default
    private List<Roadmap> roadmaps = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private UserRoles role = UserRoles.USER;

    @Embedded
    private UserSecurity userSecurity;

    @Embedded
    private UserContact userContact;


}
