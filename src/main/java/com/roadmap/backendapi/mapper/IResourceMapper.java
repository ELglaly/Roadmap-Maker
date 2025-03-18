package com.roadmap.backendapi.mapper;

import com.roadmap.backendapi.dto.ResourceDTO;
import com.roadmap.backendapi.model.Resource;

public interface IResourceMapper {

    ResourceDTO toDTO(Resource resource);
    Resource toEntity(ResourceDTO resourceDTO);
}
