package com.roadmap.backendapi.request.roadmap;

import lombok.Getter;
import lombok.Setter;

/**
 * Request class for updating a roadmap.
 * This class contains the fields that can be updated for a roadmap.
 */
@Setter
@Getter
public class UpdateRoadmapRequest {
    private Long userId;
    private Long roadmapId;
    private String title;
    private String description;
}