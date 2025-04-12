package com.roadmap.backendapi.entity;

import com.roadmap.backendapi.entity.enums.MilestoneStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

/**
 * Entity class representing a Milestone.
 * This class is used to map the Milestone entity to the database table.
 *
 * @see com.roadmap.backendapi.entity.Roadmap
 * @see com.roadmap.backendapi.entity.enums.MilestoneStatus
 */
@Setter
@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Milestone {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false,columnDefinition = "TEXT")
    private String description;

    //@Column(nullable = false)
    private Date dueDate;

    @Column(nullable = false,columnDefinition = "TEXT")
    private String actionableSteps;

    @Column(nullable = false,columnDefinition = "TEXT" )
    private String prerequisites;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false,columnDefinition = "ENUM('NOT_STARTED','IN_PROGRESS','COMPLETED')")
    private MilestoneStatus status;

    @ManyToOne
    @JoinColumn(name = "roadmap_id",nullable = false)
    private Roadmap roadmap;

    @OneToMany(mappedBy = "milestone",fetch = FetchType.LAZY,cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Resource> resources;

    @OneToMany(mappedBy = "milestone",fetch = FetchType.LAZY,cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Progress> progress;

}

