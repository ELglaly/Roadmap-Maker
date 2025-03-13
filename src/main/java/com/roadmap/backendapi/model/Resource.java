package com.roadmap.backendapi.model;

import com.roadmap.backendapi.model.enums.ResourseType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Resource {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;
    private String title;
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('VIDEO','ARTICLE','BOOK','COURSE','PODCAST')")
    private ResourseType type;
    @Column(nullable = false,columnDefinition = "TEXT")
    private String url;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "milestone_id",nullable = false)
    private Milestone milestone;
}
