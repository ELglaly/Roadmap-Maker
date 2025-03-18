package com.roadmap.backendapi.service.roadmap;

import com.roadmap.backendapi.dto.RoadmapDTO;

public interface IRoadmapSearchService {
    RoadmapDTO getRoadmapById(Long roadmapId);
    RoadmapDTO getRoadmapByUserId(Long userId);
    RoadmapDTO getRoadmapByTitle(String title);
}
