package com.roadmap.backendapi.dto;

import com.roadmap.backendapi.model.enums.ResourseType;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Builder
@EqualsAndHashCode
@Data
public class ResourceDTO {
    private Long id;
    private String title;
    private ResourseType type;
    private String url;
    private MilestoneDTO milestoneDTO;
}
