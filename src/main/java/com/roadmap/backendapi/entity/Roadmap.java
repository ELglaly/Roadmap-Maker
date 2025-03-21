package com.roadmap.backendapi.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;


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
