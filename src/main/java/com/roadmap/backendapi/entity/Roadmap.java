package com.roadmap.backendapi.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;


/**
 * Entity class representing a Roadmap.
 * This class is used to map the Roadmap table in the database.
 * It contains fields that represent the properties of a Roadmap.
 *
 * @see com.roadmap.backendapi.entity.User
 * @see com.roadmap.backendapi.entity.Milestone
 */
@Setter
@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Roadmap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;
   // @Column(nullable = false,columnDefinition = "Timestamp default current_timestamp")
    private Date createdDate;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @OneToMany(mappedBy = "roadmap",fetch = FetchType.LAZY,orphanRemoval = true,cascade = CascadeType.ALL)
    private List<Milestone> milestones;

}
