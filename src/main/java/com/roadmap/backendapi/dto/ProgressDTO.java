package com.roadmap.backendapi.dto;

import com.roadmap.backendapi.entity.Milestone;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;

/**
 * Data Transfer Object (DTO) for Progress.
 * This class is used to transfer data between the application and the client.
 * It contains fields that represent the properties of a Progress.
 *
 * @see com.roadmap.backendapi.entity.Progress
 */

@Builder
@Value
public class ProgressDTO {

    Long id;

    @NotNull(message = "Progress percentage is required")
    @DecimalMin(value = "0.00", message = "Progress percentage cannot be negative")
    @DecimalMax(value = "100.00", message = "Progress percentage cannot exceed 100%")
    @Builder.Default
    BigDecimal progressPercentage = BigDecimal.ZERO;

    @PastOrPresent(message = "Completion date cannot be in the future")
    LocalDateTime completedAt;

}
