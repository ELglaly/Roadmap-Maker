package com.roadmap.backendapi.controller;


import com.roadmap.backendapi.dto.RoadmapDTO;
import com.roadmap.backendapi.response.APIResponse;
import com.roadmap.backendapi.service.roadmap.RoadmapService;
import com.roadmap.backendapi.service.roadmap.RoadmapServiceImpl;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/roadmaps")
@Validated
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
    public ResponseEntity<APIResponse> getRoadmap(@PathVariable Long roadmapId) {
        RoadmapDTO roadmapDTO = roadmapService.getRoadmapById(roadmapId);
        return ResponseEntity.ok(new APIResponse("Roadmap fetched successfully", roadmapDTO));
    }

    /**
     * Get all roadmaps with pagination.
     *
     * @param page Page number (0-indexed, default: 0)
     * @param size Page size (1-100, default: 20)
     * @param sortBy Field to sort by (default: createdDate)
     * @param sortDir Sort direction (asc/desc, default: desc)
     * @return Paginated list of roadmaps
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllRoadmaps(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size,
            @RequestParam(defaultValue = "createdDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("asc")
            ? Sort.by(sortBy).ascending()
            : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<RoadmapDTO> roadmapPage = roadmapService.getAllRoadmaps(pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("roadmaps", roadmapPage.getContent());
        response.put("currentPage", roadmapPage.getNumber());
        response.put("totalItems", roadmapPage.getTotalElements());
        response.put("totalPages", roadmapPage.getTotalPages());
        response.put("pageSize", roadmapPage.getSize());
        response.put("hasNext", roadmapPage.hasNext());
        response.put("hasPrevious", roadmapPage.hasPrevious());

        return ResponseEntity.ok(response);
    }

    /**
     * Get roadmaps by user ID with pagination.
     *
     * @param userId User ID
     * @param page Page number (default: 0)
     * @param size Page size (default: 20)
     * @param sortBy Field to sort by (default: createdDate)
     * @param sortDir Sort direction (default: desc)
     * @return Paginated list of user's roadmaps
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<Map<String, Object>> getRoadmapsByUser(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size,
            @RequestParam(defaultValue = "createdDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("asc")
            ? Sort.by(sortBy).ascending()
            : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<RoadmapDTO> roadmapPage = roadmapService.getRoadmapByUserId(userId, pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("roadmaps", roadmapPage.getContent());
        response.put("currentPage", roadmapPage.getNumber());
        response.put("totalItems", roadmapPage.getTotalElements());
        response.put("totalPages", roadmapPage.getTotalPages());
        response.put("pageSize", roadmapPage.getSize());
        response.put("hasNext", roadmapPage.hasNext());
        response.put("hasPrevious", roadmapPage.hasPrevious());

        return ResponseEntity.ok(response);
    }

    /**
     * Search roadmaps by title with pagination.
     *
     * @param title Search term for title (partial match)
     * @param page Page number (default: 0)
     * @param size Page size (default: 20)
     * @param sortBy Field to sort by (default: createdDate)
     * @param sortDir Sort direction (default: desc)
     * @return Paginated list of matching roadmaps
     */
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchRoadmapsByTitle(
            @RequestParam String title,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size,
            @RequestParam(defaultValue = "createdDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("asc")
            ? Sort.by(sortBy).ascending()
            : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<RoadmapDTO> roadmapPage = roadmapService.getRoadmapByTitle(title, pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("roadmaps", roadmapPage.getContent());
        response.put("currentPage", roadmapPage.getNumber());
        response.put("totalItems", roadmapPage.getTotalElements());
        response.put("totalPages", roadmapPage.getTotalPages());
        response.put("pageSize", roadmapPage.getSize());
        response.put("hasNext", roadmapPage.hasNext());
        response.put("hasPrevious", roadmapPage.hasPrevious());

        return ResponseEntity.ok(response);
    }
}
