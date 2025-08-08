package com.roadmap.backendapi.dto;

import com.roadmap.backendapi.entity.enums.RoadmapStatus;
import com.roadmap.backendapi.entity.enums.RoadmapVisibility;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

/**
 * DTO for {@link com.roadmap.backendapi.entity.Roadmap}
 */
public record RoadmapDTO(
        @Size(message = "Title must be 3-255 characters", min = 3, max = 255)
        @NotBlank(message = "Title is required") String title,
        @Size(message = "Description must be 10-5000 characters", min = 10, max = 5000)
        @NotBlank(message = "Description is required") String description,
        @NotNull(message = "Status is required")
        RoadmapStatus status,
        @NotNull(message = "Visibility is required")
        RoadmapVisibility visibility) implements Serializable {
  }