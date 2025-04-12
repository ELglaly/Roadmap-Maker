package com.roadmap.backendapi.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

/**
 * Entity class representing a Progress.
 * This class is used to map the Progress table in the database.
 * It contains fields that represent the properties of a Progress.
 *
 * @see com.roadmap.backendapi.entity.User
 * @see com.roadmap.backendapi.entity.Milestone
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class Progress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp completed_at;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "milestone_id", nullable = false)
    private Milestone milestone;
}
