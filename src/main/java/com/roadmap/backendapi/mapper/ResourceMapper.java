package com.roadmap.backendapi.mapper;

import com.roadmap.backendapi.dto.ResourceDTO;
import com.roadmap.backendapi.entity.Resource;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Mapper interface for converting Resource entities to ResourceDTOs.
 * This interface uses MapStruct to generate the implementation at compile time.
 * The generated implementation will be a Spring component.
 *
 * @see com.roadmap.backendapi.entity.Resource
 * @see com.roadmap.backendapi.dto.ResourceDTO
 */
@Mapper(componentModel = "spring")
public interface ResourceMapper {
    ResourceMapper INSTANCE = Mappers.getMapper(ResourceMapper.class);

    ResourceDTO toDTO(Resource resource);
}