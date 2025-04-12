package com.roadmap.backendapi.mapper;

import com.roadmap.backendapi.dto.MilestoneDTO;
import com.roadmap.backendapi.entity.Milestone;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * Mapper interface for converting Milestone entities to MilestoneDTOs.
 * This interface uses MapStruct to generate the implementation at compile time.
 * The generated implementation will be a Spring component.
 *
 * @see com.roadmap.backendapi.entity.Milestone
 * @see com.roadmap.backendapi.dto.MilestoneDTO
 */
@Mapper(componentModel = "spring")
public interface MilestoneMapper {
  MilestoneMapper INSTANCE = Mappers.getMapper(MilestoneMapper.class);

  @Mapping(target = "resourcesDTO", source = "resources")
  MilestoneDTO toDTO(Milestone milestone);

}
