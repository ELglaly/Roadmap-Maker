package com.roadmap.backendapi.dto;

import com.roadmap.backendapi.model.Milestone;
import com.roadmap.backendapi.model.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Timestamp;
import java.util.List;

@Builder
@EqualsAndHashCode
@Data
public class RoadmapDTO {
    // Attributes from the Roadmap entity
    private Long id;
    private String title;
    private String description;
    private List<MilestoneDTO> milestonesDTO;
}
