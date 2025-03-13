package com.roadmap.backendapi.dto;


import com.roadmap.backendapi.model.enums.MilestoneStatus;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Timestamp;
import java.util.List;

@Data
@Builder
@EqualsAndHashCode
public class MilestoneDTO {
    private Long id;

    private String title;

    private String description;

    private Timestamp dueDate;

    private MilestoneStatus status;

    private RoadmapDTO roadmapDTO;

    private List<ResourceDTO> resourcesDTO;

}


