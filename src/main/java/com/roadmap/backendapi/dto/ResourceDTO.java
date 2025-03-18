package com.roadmap.backendapi.dto;

import com.roadmap.backendapi.model.enums.ResourceType;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Builder
@EqualsAndHashCode
@Data
public class ResourceDTO {
    private Long id;
    private String title;
    private ResourceType type;
    private String url;
}
