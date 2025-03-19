package com.roadmap.backendapi.mapper;

import com.roadmap.backendapi.dto.MilestoneDTO;
import com.roadmap.backendapi.dto.RoadmapDTO;
import com.roadmap.backendapi.model.Milestone;
import com.roadmap.backendapi.model.Roadmap;
import lombok.RequiredArgsConstructor;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;


@Mapper(componentModel = "spring",uses = {MilestoneMapper.class})
public interface RoadmapMapper
{
    RoadmapMapper INSTANCE = Mappers.getMapper(RoadmapMapper.class);

    @Mapping(target = "milestonesDTO" , source = "milestones")

    RoadmapDTO toDTO(Roadmap roadmap);
}