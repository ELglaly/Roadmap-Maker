package com.roadmap.backendapi.mapper;

import com.roadmap.backendapi.dto.RoadmapDTO;
import com.roadmap.backendapi.model.Roadmap;

public interface IRoadmapMapper {
    RoadmapDTO toDTO(Roadmap roadmap);
    Roadmap toEntity(RoadmapDTO roadmapDTO);
}