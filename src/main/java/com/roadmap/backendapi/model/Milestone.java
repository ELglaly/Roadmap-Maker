package com.roadmap.backendapi.model;

import com.roadmap.backendapi.model.enums.MilestoneStatus;
import jakarta.persistence.*;
import lombok.*;
import org.json.JSONArray;
import org.json.JSONPropertyIgnore;

import java.sql.Timestamp;
import java.util.List;

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
    private String title;

    @Column(nullable = false,columnDefinition = "TEXT")
    private String description;

    //@Column(nullable = false)
    private Timestamp dueDate;

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

