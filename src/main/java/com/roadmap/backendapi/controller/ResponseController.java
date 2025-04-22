package com.roadmap.backendapi.controller;

import com.roadmap.backendapi.response.APIResponse;
import com.roadmap.backendapi.service.response.ResponseCacheService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for handling response-related operations.
 * This controller provides endpoints for retrieving previous responses.
 */
@RestController
@RequestMapping("/api/v1/responses")
public class ResponseController {

    private final ResponseCacheService responseCacheService;

    /**
     * Constructor for ResponseController.
     *
     * @param responseCacheService the ResponseCacheService used to retrieve previous responses
     */
    public ResponseController(ResponseCacheService responseCacheService) {
        this.responseCacheService = responseCacheService;
    }

    /**
     * Retrieves the previous response for a specific user.
     *
     * @param userId the ID of the user
     * @return a ResponseEntity containing the previous response, or a 404 if no previous response exists
     */
    @GetMapping("/previous/{userId}")
    public ResponseEntity<APIResponse> getPreviousResponse(@PathVariable String userId) {
        APIResponse previousResponse = responseCacheService.getPreviousResponse(userId);
        
        if (previousResponse == null) {
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new APIResponse("No previous response found", null));
        }
        
        return ResponseEntity.ok(previousResponse);
    }
}