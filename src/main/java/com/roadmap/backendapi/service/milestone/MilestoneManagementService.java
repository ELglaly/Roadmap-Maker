package com.roadmap.backendapi.service.milestone;

import com.roadmap.backendapi.dto.MilestoneDTO;
import com.roadmap.backendapi.entity.Roadmap;
import com.roadmap.backendapi.request.milestone.UpdateMilestoneRequest;

public interface MilestoneManagementService {
    void updateMilestones(Roadmap roadmap);
    void deleteMilestone(Long milestoneId);
}
