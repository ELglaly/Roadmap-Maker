package com.roadmap.backendapi.service.milestone;

import com.roadmap.backendapi.dto.MilestoneDTO;
import com.roadmap.backendapi.entity.enums.MilestoneStatus;

import java.util.List;


public interface MilestoneSearchService {
    MilestoneDTO getMilestoneById(Long milestoneId);
    MilestoneDTO getMilestoneByTitle(String title);
    List<MilestoneDTO> getMilestoneByRoadmapId(Long roadmapId);
    MilestoneDTO getMilestoneByStatus(MilestoneStatus status);
    MilestoneDTO getMilestoneByStatus(String status);
}
