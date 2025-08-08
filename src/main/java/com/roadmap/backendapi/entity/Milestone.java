package com.roadmap.backendapi.entity;

import com.roadmap.backendapi.entity.enums.MilestoneStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
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
    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 100, message = "Title must be 3-100 characters")
    @Pattern(regexp = "^[A-Za-z0-9\\s'-]{3,100}$", message = "Invalid characters in title")
    private String title;

    @Column(nullable = false,columnDefinition = "TEXT")
    @NotBlank(message = "Description is required")
    @Size(min = 10, max = 500, message = "Description must be 10-500 characters")
    @Pattern(regexp = "^[A-Za-z0-9\\s'.,-]{10,500}$", message = "Invalid characters in description")
    @Size(max = 500, message = "Description too long")
    private String description;


    @Column(nullable = false)
    @NotBlank(message = "Due date is required")
    @Temporal(TemporalType.DATE)
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Due date must be in the format YYYY-MM-DD")
    private Date dueDate;

    @Column(nullable = false)
    @ElementCollection
    @Builder.Default
    private List<String> actionableSteps = new ArrayList<>();

    @Column(nullable = false)
    @ElementCollection
    @Builder.Default
    private List<String> prerequisites =new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MilestoneStatus status;

    @ManyToOne
    @JoinColumn(name = "roadmap_id",nullable = false)
    private Roadmap roadmap;

    @OneToMany(mappedBy = "milestone",fetch = FetchType.LAZY,cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Resource> resources;

    @OneToOne(cascade = CascadeType.ALL,orphanRemoval = true,
            fetch = FetchType.LAZY)
    @JoinColumn(name = "progress_id", nullable = false)
    private Progress progress;

}

