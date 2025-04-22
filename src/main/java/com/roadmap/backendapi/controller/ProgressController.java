package com.roadmap.backendapi.controller;

import com.roadmap.backendapi.dto.ProgressDTO;
import com.roadmap.backendapi.request.progress.CreateProgressRequest;
import com.roadmap.backendapi.request.progress.UpdateProgressRequest;
import com.roadmap.backendapi.response.APIResponse;
import com.roadmap.backendapi.service.progress.ProgressService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing Progress resources.
 * This controller provides endpoints for creating, updating, deleting, and retrieving progress.
 */
@RestController
@RequestMapping("/api/progress")
public class ProgressController {

    private final ProgressService progressService;

    public ProgressController(ProgressService progressService) {
        this.progressService = progressService;
    }

    /**
     * POST endpoint to create a new progress.
     * This endpoint creates a progress without fetching the user from the database.
     * It only requires the user ID and milestone ID.
     *
     * @param request The request containing the user ID, milestone ID, and optional completed_at timestamp
     * @return ResponseEntity containing the created progress DTO
     */
    @PostMapping
    public ResponseEntity<APIResponse> createProgress(@RequestBody CreateProgressRequest request) {
        ProgressDTO progressDTO = progressService.createProgress(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new APIResponse( "Progress created successfully",progressDTO));
    }

    /**
     * PUT endpoint to update an existing progress.
     *
     * @param request The request containing the progress ID and updated fields
     * @return ResponseEntity containing the updated progress DTO
     */
    @PutMapping
    public ResponseEntity<APIResponse> updateProgress(@RequestBody UpdateProgressRequest request) {
        ProgressDTO progressDTO = progressService.updateProgress(request);
        return ResponseEntity.ok(new APIResponse( "Progress updated successfully",progressDTO));
    }

    /**
     * DELETE endpoint to delete a progress by ID.
     *
     * @param progressId The ID of the progress to delete
     * @return ResponseEntity with a success message
     */
    @DeleteMapping("/{progressId}")
    public ResponseEntity<APIResponse> deleteProgress(@PathVariable Long progressId) {
        progressService.deleteProgress(progressId);
        return ResponseEntity.ok(new APIResponse(null, "Progress deleted successfully"));
    }

    /**
     * GET endpoint to retrieve a progress by ID.
     *
     * @param progressId The ID of the progress to retrieve
     * @return ResponseEntity containing the progress DTO
     */
    @GetMapping("/{progressId}")
    public ResponseEntity<APIResponse> getProgressById(@PathVariable Long progressId) {
        ProgressDTO progressDTO = progressService.getProgressById(progressId);
        return ResponseEntity.ok(new APIResponse("Progress retrieved successfully",progressDTO));
    }

    /**
     * GET endpoint to retrieve a progress by user ID.
     *
     * @param userId The ID of the user whose progress to retrieve
     * @return ResponseEntity containing the progress DTO
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<APIResponse> getProgressByUserId(@PathVariable Long userId) {
        ProgressDTO progressDTO = progressService.getProgressByUserId(userId);
        return ResponseEntity.ok(new APIResponse( "Progress retrieved successfully",progressDTO));
    }

    /**
     * GET endpoint to retrieve a progress by milestone ID.
     *
     * @param milestoneId The ID of the milestone whose progress to retrieve
     * @return ResponseEntity containing the progress DTO
     */
    @GetMapping("/milestone/{milestoneId}")
    public ResponseEntity<APIResponse> getProgressByMilestoneId(@PathVariable Long milestoneId) {
        ProgressDTO progressDTO = progressService.getProgressByMilestoneId(milestoneId);
        return ResponseEntity.ok(new APIResponse( "Progress retrieved successfully",progressDTO));
    }
}