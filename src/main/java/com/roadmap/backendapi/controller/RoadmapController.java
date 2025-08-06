package com.roadmap.backendapi.controller;


import com.roadmap.backendapi.dto.RoadmapDTO;
import com.roadmap.backendapi.response.APIResponse;
import com.roadmap.backendapi.service.roadmap.RoadmapService;
import com.roadmap.backendapi.service.roadmap.RoadmapServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/roadmaps")
public class RoadmapController {

    private final RoadmapService roadmapService;


    public RoadmapController(RoadmapService roadmapService) {
        this.roadmapService = roadmapService;
    }


    @GetMapping("/create/{userId}")
    public ResponseEntity<APIResponse> createRoadmap(@PathVariable Long userId) {
        RoadmapDTO roadmapDTO = roadmapService.generateRoadmap(userId);
        return new ResponseEntity<>(new APIResponse("Roadmap created successfully", roadmapDTO), HttpStatus.CREATED);
    }
    @GetMapping("/{roadmapId}")
    public ResponseEntity<APIResponse> getRoadmap( @PathVariable Long roadmapId) {
        RoadmapDTO roadmapDTO= roadmapService.getRoadmapById(roadmapId);
        return ResponseEntity.ok(new APIResponse("Roadmap fetched successfully",roadmapDTO));

    }
}
