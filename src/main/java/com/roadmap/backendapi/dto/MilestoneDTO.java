package com.roadmap.backendapi.dto;

import com.roadmap.backendapi.entity.enums.MilestoneStatus;
import jakarta.validation.constraints.*;
import lombok.Value;
import org.hibernate.annotations.Immutable;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * DTO for {@link com.roadmap.backendapi.entity.Milestone}
 */
public record MilestoneDTO(
        @Size(message = "Title must be 3-100 characters", min = 3, max = 100)
        @Pattern(message = "Invalid characters in title", regexp = "^[A-Za-z0-9\\s'-]{3,100}$")
        @NotBlank(message = "Title is required")
        String title,

        @Size(message = "Description must be 10-500 characters", min = 10, max = 500)
        @Pattern(message = "Invalid characters in description", regexp = "^[A-Za-z0-9\\s'.,-]{10,500}$")
        @NotBlank(message = "Description is required")
        String description,

        @Future(message = "Due date must be in the future")
        Date dueDate,

        List<@NotBlank @Size(max = 200)String> actionableSteps,
        List<@NotBlank @Size(max = 200)String> prerequisites,
        @NotNull MilestoneStatus status)
        implements Serializable {

     /*
     Compact constructor ensuring list immutability
     */
    public MilestoneDTO {
        // Create truly immutable lists
        actionableSteps = actionableSteps != null ?
                List.copyOf(actionableSteps) : List.of();
        prerequisites = prerequisites != null ?
                List.copyOf(prerequisites) : List.of();
    }
}