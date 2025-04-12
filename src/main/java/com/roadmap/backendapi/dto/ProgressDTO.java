package com.roadmap.backendapi.dto;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Timestamp;

/**
 * Data Transfer Object (DTO) for Progress.
 * This class is used to transfer data between the application and the client.
 * It contains fields that represent the properties of a Progress.
 *
 * @see com.roadmap.backendapi.entity.Progress
 */

@Builder
@EqualsAndHashCode
@Data
public class ProgressDTO {
    private Long id;

    private Timestamp completed_at;

}
