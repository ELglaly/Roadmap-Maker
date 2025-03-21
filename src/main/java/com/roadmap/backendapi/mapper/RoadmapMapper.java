package com.roadmap.backendapi.mapper;

import com.roadmap.backendapi.dto.RoadmapDTO;
import com.roadmap.backendapi.entity.Roadmap;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;


@Mapper(componentModel = "spring",uses = {MilestoneMapper.class})
public interface RoadmapMapper
{
    RoadmapMapper INSTANCE = Mappers.getMapper(RoadmapMapper.class);

    @Mapping(target = "milestonesDTO" , source = "milestones")

    RoadmapDTO toDTO(Roadmap roadmap);
}