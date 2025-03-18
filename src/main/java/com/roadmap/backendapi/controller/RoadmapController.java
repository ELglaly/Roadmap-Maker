package com.roadmap.backendapi.controller;


import com.roadmap.backendapi.dto.RoadmapDTO;
import com.roadmap.backendapi.model.Roadmap;
import com.roadmap.backendapi.service.roadmap.IRoadmapService;
import com.roadmap.backendapi.service.roadmap.RoadmapService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.spring.web.json.Json;

@RestController
@RequestMapping("/api/v1/roadmaps")
public class RoadmapController {

    private final RoadmapService roadmapService;

    public RoadmapController(RoadmapService roadmapService) {
        this.roadmapService = roadmapService;
    }


    @GetMapping("/create/{userId}")
    public ResponseEntity<RoadmapDTO> createRoadmap( @PathVariable Long userId) {
        RoadmapDTO roadmapDTO= roadmapService.createRoadmap(userId);
        return ResponseEntity.ok(roadmapDTO);
    }
    @GetMapping("/{roadmapId}")
    public ResponseEntity<RoadmapDTO> getRoadmap( @PathVariable Long roadmapId) {
        RoadmapDTO roadmapDTO= roadmapService.getRoadmapById(roadmapId);
        return ResponseEntity.ok(roadmapDTO);
    }
}
