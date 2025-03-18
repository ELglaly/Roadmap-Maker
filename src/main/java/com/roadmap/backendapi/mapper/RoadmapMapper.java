package com.roadmap.backendapi.mapper;

import com.roadmap.backendapi.dto.RoadmapDTO;
import com.roadmap.backendapi.model.Roadmap;
import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component

public class RoadmapMapper implements IRoadmapMapper
{
    private final ModelMapper modelMapper;

    public RoadmapMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }


    @Override
    public RoadmapDTO toDTO(Roadmap roadmap) {
        return modelMapper.map(roadmap, RoadmapDTO.class);
    }

    @Override
    public Roadmap toEntity(RoadmapDTO roadmapDTO) {
        return modelMapper.map(roadmapDTO, Roadmap.class);
    }

}