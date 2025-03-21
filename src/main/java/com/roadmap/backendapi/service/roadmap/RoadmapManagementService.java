package com.roadmap.backendapi.service.roadmap;

import com.roadmap.backendapi.dto.RoadmapDTO;
import com.roadmap.backendapi.request.roadmap.UpdateRoadmapRequest;

public interface RoadmapManagementService {
    RoadmapDTO generateRoadmap(Long userId);
    RoadmapDTO updateRoadmap(UpdateRoadmapRequest request);
    void deleteRoadmap(Long roadmapId);
}
