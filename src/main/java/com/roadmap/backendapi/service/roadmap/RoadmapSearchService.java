package com.roadmap.backendapi.service.roadmap;

import com.roadmap.backendapi.dto.RoadmapDTO;

import java.util.List;

public interface RoadmapSearchService {
    RoadmapDTO getRoadmapById(Long roadmapId);
    List<RoadmapDTO> getRoadmapByUserId(Long userId);
    List<RoadmapDTO> getRoadmapByTitle(String title);
}
