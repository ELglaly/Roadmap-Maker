package com.roadmap.backendapi.service.milestone;

import com.roadmap.backendapi.dto.MilestoneDTO;
import com.roadmap.backendapi.entity.enums.MilestoneStatus;

import java.util.List;


/**
 * MilestoneSearchService is an interface that defines methods for searching milestones in a roadmap.
 * It includes methods for retrieving milestones by various criteria such as ID, title, roadmap ID, and status.
 */
public interface MilestoneSearchService {
    MilestoneDTO getMilestoneById(Long milestoneId);
    MilestoneDTO getMilestoneByTitle(String title);
    List<MilestoneDTO> getMilestoneByRoadmapId(Long roadmapId);
    MilestoneDTO getMilestoneByStatus(MilestoneStatus status);
    MilestoneDTO getMilestoneByStatus(String status);
}
