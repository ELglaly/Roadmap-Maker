package com.roadmap.backendapi.mapper;

import com.roadmap.backendapi.dto.ResourceDTO;
import com.roadmap.backendapi.model.Resource;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ResourceMapper {
    ResourceMapper INSTANCE = Mappers.getMapper(ResourceMapper.class);

    @Mapping(target = "milestone", source ="milestoneDTO" )
    ResourceDTO toDTO(Resource resource);
}
