package com.roadmap.backendapi.mapper;

import com.roadmap.backendapi.dto.ResourceDTO;
import com.roadmap.backendapi.entity.Resource;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ResourceMapper {
    ResourceMapper INSTANCE = Mappers.getMapper(ResourceMapper.class);

    ResourceDTO toDTO(Resource resource);
}