package com.roadmap.backendapi.service.resource;

import com.roadmap.backendapi.dto.ResourceDTO;
import com.roadmap.backendapi.entity.Milestone;
import com.roadmap.backendapi.entity.Roadmap;
import com.roadmap.backendapi.request.resource.UpdateResourceRequest;

public interface ResourceManagementService {
    void addResourcesToMilestone(Milestone milestone, Roadmap roadmap);
    ResourceDTO updateResource(UpdateResourceRequest request);
    void deleteResource(Long resourceId);
}
