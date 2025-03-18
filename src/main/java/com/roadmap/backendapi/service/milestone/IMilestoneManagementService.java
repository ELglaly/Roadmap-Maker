package com.roadmap.backendapi.service.milestone;

import com.roadmap.backendapi.dto.MilestoneDTO;
import com.roadmap.backendapi.model.Milestone;
import com.roadmap.backendapi.model.Roadmap;
import com.roadmap.backendapi.request.milestone.CreateMilestoneRequest;
import com.roadmap.backendapi.request.milestone.UpdateMilestoneRequest;

import java.util.List;

public interface IMilestoneManagementService {
    MilestoneDTO updateMilestone(UpdateMilestoneRequest request);
    List<Milestone> createMilestone(Roadmap roadmap);
    void deleteMilestone(Long milestoneId);
}
