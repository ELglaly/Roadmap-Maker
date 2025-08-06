package com.roadmap.backendapi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;
import java.time.LocalDateTime;
import java.math.BigDecimal;

/**
 * Entity class representing a Progress.
 * Tracks user progress on milestones with percentage completion.
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

    @Column(precision = 5, scale = 2, nullable = false)
    @NotNull(message = "Progress percentage is required")
    @DecimalMin(value = "0.00", message = "Progress percentage cannot be negative")
    @DecimalMax(value = "100.00", message = "Progress percentage cannot exceed 100%")
    @Builder.Default
    private BigDecimal progressPercentage = BigDecimal.ZERO;

    @Column(name = "completed_at")
    @PastOrPresent(message = "Completion date cannot be in the future")
    private LocalDateTime completedAt;

    @LastModifiedDate
    @Column(name = "last_modified_date")
    private Instant lastModifiedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "milestone_id", nullable = false)
    @NotNull(message = "Milestone is required")
    private Milestone milestone;

    @CreatedDate
    @Column(name = "created_date")
    private Instant createdDate;

}
