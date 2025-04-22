package com.roadmap.backendapi.request.roadmap;

/**
 * Request class for updating a roadmap.
 * This class contains the fields that can be updated for a roadmap.
 */
public class UpdateRoadmapRequest {
    private Long userId;
    private Long roadmapId;
    private String title;
    private String description;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRoadmapId() {
        return roadmapId;
    }

    public void setRoadmapId(Long roadmapId) {
        this.roadmapId = roadmapId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}