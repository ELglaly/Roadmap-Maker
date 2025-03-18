package com.roadmap.backendapi.repository;

import com.roadmap.backendapi.model.Progress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProgressRepository extends JpaRepository<Progress, Long> {
    Progress findByUserId(Long userId);
    Boolean existsByUserId(Long userId);

    Progress findByMilestoneId(Long milestoneId);
}
