package com.roadmap.backendapi.service.resource;

import com.roadmap.backendapi.dto.ResourceDTO;
import com.roadmap.backendapi.model.Milestone;
import com.roadmap.backendapi.model.Resource;
import com.roadmap.backendapi.model.Roadmap;
import com.roadmap.backendapi.request.resource.CreateResourceRequest;
import com.roadmap.backendapi.request.resource.UpdateResourceRequest;

import java.util.List;

public interface IResourceManagementService {
    List<Resource> createResource(Milestone milestone, Roadmap roadmap);
    ResourceDTO updateResource(UpdateResourceRequest request);
    void deleteResource(Long resourceId);
}
