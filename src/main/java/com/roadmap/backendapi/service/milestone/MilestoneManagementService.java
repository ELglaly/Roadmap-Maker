package com.roadmap.backendapi.service.milestone;

import com.roadmap.backendapi.dto.MilestoneDTO;
import com.roadmap.backendapi.entity.Roadmap;
import com.roadmap.backendapi.request.milestone.UpdateMilestoneRequest;

/**
 * MilestoneManagementService is an interface that defines methods for managing milestones in a roadmap.
 * It includes methods for updating and deleting milestones.
 */
public interface MilestoneManagementService {
    void updateMilestones(Roadmap roadmap);
    void deleteMilestone(Long milestoneId);
}
