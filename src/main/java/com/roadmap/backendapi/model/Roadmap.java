package com.roadmap.backendapi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Roadmap {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    @Column(nullable = false,columnDefinition = "Timestamp default current_timestamp")
    private Timestamp createdDate;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @OneToMany(mappedBy = "roadmap",orphanRemoval = true,cascade = CascadeType.ALL)
    private List<Milestone> milestones;
}
