package com.roadmap.backendapi.repository;

import com.roadmap.backendapi.model.Milestone;
import com.roadmap.backendapi.model.enums.MilestoneStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MilestoneRepository extends JpaRepository<Milestone, Long> {
    Milestone findByTitle(String title);
    Boolean existsByTitle(String title);

    List<Milestone> findByRoadmapId(Long roadmapId);

    Milestone findByStatus(MilestoneStatus status);
}
