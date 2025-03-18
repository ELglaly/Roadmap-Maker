package com.roadmap.backendapi.dto;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Timestamp;

@Builder
@EqualsAndHashCode
@Data
public class ProgressDTO {
    private Long id;

    private Timestamp completed_at;

}
