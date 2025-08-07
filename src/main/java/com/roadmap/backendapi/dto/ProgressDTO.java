package com.roadmap.backendapi.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO for {@link com.roadmap.backendapi.entity.Progress}
 */
public record ProgressDTO(@NotNull(message = "Progress percentage is required") BigDecimal progressPercentage, @PastOrPresent(message = "Completion date cannot be in the future") LocalDateTime completedAt) implements Serializable {
  }