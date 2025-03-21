package com.roadmap.backendapi.controller;


import com.roadmap.backendapi.dto.MilestoneDTO;
import com.roadmap.backendapi.response.APIResponse;
import com.roadmap.backendapi.service.milestone.MilestoneService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/milestone")
public class MilestoneController {

    private final MilestoneService milestoneService;

    public MilestoneController( MilestoneService milestoneService) {
        this.milestoneService = milestoneService;
    }


    @GetMapping("/{roadmapId}")
    public ResponseEntity<APIResponse> getMilestoneByRoadmap(@PathVariable Long roadmapId) {
        List<MilestoneDTO> milestonesDTO = milestoneService.getMilestoneByRoadmapId(roadmapId);
        return ResponseEntity.ok(new APIResponse("Milestone fetched successfully",milestonesDTO));

    }
}
