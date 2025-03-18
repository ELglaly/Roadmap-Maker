package com.roadmap.backendapi.service.resource;

import com.roadmap.backendapi.dto.ResourceDTO;

import java.util.List;

public interface IResourceSearchService {
    ResourceDTO getResourceById(Long resourceId);
    ResourceDTO getResourceByTitle(String title);
    ResourceDTO getResourceByType(String type);
    ResourceDTO getResourceByUrl(String url);
    List<ResourceDTO> getResourceByMilestoneId(Long milestoneId);
}
