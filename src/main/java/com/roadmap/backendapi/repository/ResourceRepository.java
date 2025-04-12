package com.roadmap.backendapi.repository;

import com.roadmap.backendapi.entity.Resource;
import com.roadmap.backendapi.entity.enums.ResourceType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository interface for managing Resource entities.
 * This interface extends JpaRepository to provide CRUD operations
 * and custom query methods for Resource entities.
 *
 * @see com.roadmap.backendapi.entity.Resource
 * @see org.springframework.data.jpa.repository.JpaRepository
 */
public interface ResourceRepository extends JpaRepository<Resource, Long> {
    Resource findByTitle(String title);
    Boolean existsByTitle(String title);

    Resource findByType(ResourceType resourceType);

    List<Resource> findByMilestoneId(Long milestoneId);

    Resource findByUrl(String url);
}
