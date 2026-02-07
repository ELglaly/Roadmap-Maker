package com.roadmap.backendapi.entity;

import com.roadmap.backendapi.entity.enums.ResourceType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.validator.constraints.URL;


/**
 * Entity class representing a Resource.
 * This class is used to map the Resource table in the database.
 * It contains fields that represent the properties of a Resource.
 *
 * @see com.roadmap.backendapi.entity.Milestone
 */

@Entity
@Table(
    name = "resource",
    indexes = {
        @Index(name = "idx_resource_milestone_id", columnList = "milestone_id"),
        @Index(name = "idx_resource_type", columnList = "type"),
        @Index(name = "idx_resource_milestone_type", columnList = "milestone_id, type")
    }
)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class Resource {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;


    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title cannot exceed 255 characters")
    private String title;

    @NotNull(message = "Resource type is required")
    @Enumerated(EnumType.STRING)
    private ResourceType type;

    @NotBlank(message = "URL is required")
    @URL(message = "Invalid URL format")
    @Size(max = 2048, message = "URL too long")
    @Column(nullable = false, columnDefinition = "TEXT")
    @Pattern(regexp = "^https?://.*", message = "Only HTTP/HTTPS URLs are allowed")
    private String url;

    @ManyToOne()
    @JoinColumn(name = "milestone_id", nullable = false)
    private Milestone milestone;
}
