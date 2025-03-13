package com.roadmap.backendapi.mapper;

import com.roadmap.backendapi.dto.MilestoneDTO;
import com.roadmap.backendapi.model.Milestone;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MilestoneMapper {
    MilestoneMapper INSTANCE = Mappers.getMapper(MilestoneMapper.class);
    @Mapping(target = "roadmapDTO", source = "roadmap")
    @Mapping(target = "resourcesDTO", source = "resources")
    MilestoneDTO toDTO(Milestone milestone);
}
