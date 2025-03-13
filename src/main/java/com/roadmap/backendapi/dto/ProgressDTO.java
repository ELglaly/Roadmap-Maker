package com.roadmap.backendapi.dto;

import com.roadmap.backendapi.model.Milestone;
import com.roadmap.backendapi.model.User;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

    private MilestoneDTO milestoneDTO;
}
