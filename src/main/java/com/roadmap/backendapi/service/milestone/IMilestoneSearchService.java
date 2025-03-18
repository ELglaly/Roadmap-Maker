package com.roadmap.backendapi.service.milestone;

import com.roadmap.backendapi.dto.MilestoneDTO;
import com.roadmap.backendapi.model.enums.MilestoneStatus;

import java.util.List;


public interface IMilestoneSearchService {
    MilestoneDTO getMilestoneById(Long milestoneId);
    MilestoneDTO getMilestoneByTitle(String title);
    List<MilestoneDTO> getMilestoneByRoadmapId(Long roadmapId);
    MilestoneDTO getMilestoneByStatus(MilestoneStatus status);
    MilestoneDTO getMilestoneByStatus(String status);
}
