package com.roadmap.backendapi.mapper;

import com.roadmap.backendapi.dto.MilestoneDTO;
import com.roadmap.backendapi.entity.Milestone;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MilestoneMapper {
  MilestoneMapper INSTANCE = Mappers.getMapper(MilestoneMapper.class);

  @Mapping(target = "resourcesDTO", source = "resources")
  MilestoneDTO toDTO(Milestone milestone);

}
