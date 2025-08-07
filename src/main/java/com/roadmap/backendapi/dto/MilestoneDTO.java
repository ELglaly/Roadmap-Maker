package com.roadmap.backendapi.dto;

import com.roadmap.backendapi.entity.enums.MilestoneStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Value;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * DTO for {@link com.roadmap.backendapi.entity.Milestone}
 */
public record MilestoneDTO(
        @Size(message = "Title must be 3-100 characters", min = 3, max = 100) @Pattern(message = "Invalid characters in title", regexp = "^[A-Za-z0-9\\s'-]{3,100}$") @NotBlank(message = "Title is required") String title,
        @Size(message = "Description must be 10-500 characters", min = 10, max = 500) @Pattern(message = "Invalid characters in description", regexp = "^[A-Za-z0-9\\s'.,-]{10,500}$") @NotBlank(message = "Description is required") String description,
        Date dueDate, List<String> actionableSteps, List<String> prerequisites,
        @NotNull MilestoneStatus status) implements Serializable {
}