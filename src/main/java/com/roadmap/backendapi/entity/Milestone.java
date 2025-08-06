package com.roadmap.backendapi.entity;

import com.roadmap.backendapi.entity.enums.MilestoneStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
public class Milestone {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank
    private String title;

    @Column(nullable = false,columnDefinition = "TEXT")
    @NotBlank
    private String description;

    @Column(nullable = false)
    @NotBlank
    private Date dueDate;

    @Column(nullable = false, columnDefinition = "TEXT")
    @NotBlank
    private String actionableSteps;

    @Column(nullable = false,columnDefinition = "TEXT" )
    @NotBlank
    private String prerequisites;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MilestoneStatus status;

    @ManyToOne
    @JoinColumn(name = "roadmap_id",nullable = false)
    private Roadmap roadmap;

    @OneToMany(mappedBy = "milestone",fetch = FetchType.LAZY,cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Resource> resources;

    @OneToMany(mappedBy = "milestone",fetch = FetchType.LAZY,cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Progress> progress;

}

