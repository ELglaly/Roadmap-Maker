package com.roadmap.backendapi.service.roadmap;

import com.roadmap.backendapi.dto.RoadmapDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RoadmapSearchService {
    RoadmapDTO getRoadmapById(Long roadmapId);
    List<RoadmapDTO> getRoadmapByUserId(Long userId);
    List<RoadmapDTO> getRoadmapByTitle(String title);

    // Paginated versions
    Page<RoadmapDTO> getRoadmapByUserId(Long userId, Pageable pageable);
    Page<RoadmapDTO> getRoadmapByTitle(String title, Pageable pageable);
    Page<RoadmapDTO> getAllRoadmaps(Pageable pageable);
}
