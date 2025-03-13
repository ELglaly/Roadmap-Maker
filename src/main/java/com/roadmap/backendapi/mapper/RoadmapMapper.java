package com.roadmap.backendapi.mapper;

import com.roadmap.backendapi.dto.RoadmapDTO;
import com.roadmap.backendapi.model.Roadmap;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RoadmapMapper {
    RoadmapMapper INSTANCE = Mappers.getMapper(RoadmapMapper.class);

    @Mapping(target = "milestonesDTO", source = "milestones")
    @Mapping(target = "userDTO", source = "user")

    RoadmapDTO toDTO(Roadmap roadmap);
}