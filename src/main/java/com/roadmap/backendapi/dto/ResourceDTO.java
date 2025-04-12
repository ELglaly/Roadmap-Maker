package com.roadmap.backendapi.dto;

import com.roadmap.backendapi.entity.enums.ResourceType;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * Data Transfer Object (DTO) for Resource.
 * This class is used to transfer data between the application and the client.
 * It contains fields that represent the properties of a Resource.
 *
 * @see ResourceType
 * @see com.roadmap.backendapi.entity.Resource
 */

@Builder
@EqualsAndHashCode
@Data
public class ResourceDTO {
    private Long id;
    private String title;
    private ResourceType type;
    private String url;
}
