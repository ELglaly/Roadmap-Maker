package com.roadmap.backendapi.entity;

import com.roadmap.backendapi.entity.enums.ResourceType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;


/**
 * Entity class representing a Resource.
 * This class is used to map the Resource table in the database.
 * It contains fields that represent the properties of a Resource.
 *
 * @see com.roadmap.backendapi.entity.Milestone
 */

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

    @ManyToOne
    @JoinColumn(name = "milestone_id",nullable = false)
    private Milestone milestone;

}
