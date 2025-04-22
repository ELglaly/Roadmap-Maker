package com.roadmap.backendapi.repository;

import com.roadmap.backendapi.entity.Progress;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing Progress entities.
 * This interface extends JpaRepository to provide CRUD operations
 * and custom query methods for Progress entities.
 *
 * @see com.roadmap.backendapi.entity.Progress
 * @see org.springframework.data.jpa.repository.JpaRepository
 */
public interface ProgressRepository extends JpaRepository<Progress, Long> {
    Progress findByUserId(Long userId);
    Boolean existsByUserId(Long userId);

    Progress findByMilestoneId(Long milestoneId);

    Boolean existsByUserIdAndMilestoneId(Long userId, Long milestoneId);
}
