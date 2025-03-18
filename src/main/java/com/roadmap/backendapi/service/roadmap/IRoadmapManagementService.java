package com.roadmap.backendapi.service.roadmap;

import com.roadmap.backendapi.dto.RoadmapDTO;
import com.roadmap.backendapi.model.Roadmap;
import com.roadmap.backendapi.request.roadmap.CreateRoadmapRequest;
import com.roadmap.backendapi.request.roadmap.UpdateRoadmapRequest;

public interface IRoadmapManagementService {
    RoadmapDTO createRoadmap(Long userId);
    RoadmapDTO updateRoadmap(UpdateRoadmapRequest request);
    void deleteRoadmap(Long roadmapId);
}
