package com.roadmap.backendapi.mapper;

import com.roadmap.backendapi.dto.RoadmapDTO;
import com.roadmap.backendapi.entity.Roadmap;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * Mapper interface for converting Roadmap entities to RoadmapDTOs.
 * This interface uses MapStruct to generate the implementation at compile time.
 * The generated implementation will be a Spring component.
 *
 * @see com.roadmap.backendapi.entity.Roadmap
 * @see com.roadmap.backendapi.dto.RoadmapDTO
 */

@Mapper(componentModel = "spring",uses = {MilestoneMapper.class})
public interface RoadmapMapper
{
    RoadmapMapper INSTANCE = Mappers.getMapper(RoadmapMapper.class);


    RoadmapDTO toDTO(Roadmap roadmap);
}