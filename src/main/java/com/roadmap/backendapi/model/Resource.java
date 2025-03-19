package com.roadmap.backendapi.model;

import com.roadmap.backendapi.model.enums.ResourceType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class Resource {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;
    private String title;
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('VIDEO','ARTICLE','BOOK','COURSE'," +
            "'PODCAST', 'PRACTICE' ,'WEBSITE' ,'TUTORIAL' ,'TOOL')")
    private ResourceType type;
    @Column(nullable = false,columnDefinition = "TEXT")
    private String url;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "milestone_id",nullable = false)
    private Milestone milestone;
}
