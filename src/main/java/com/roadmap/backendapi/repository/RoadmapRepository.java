package com.roadmap.backendapi.repository;

import com.roadmap.backendapi.model.Roadmap;
import com.roadmap.backendapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoadmapRepository extends JpaRepository<Roadmap, Long> {
    Roadmap findByTitle(String title);
    Boolean existsByTitle(String title);
    Roadmap findByUser(User user);
    Boolean existsByUserId(Long userId);

    Roadmap findByUserId(Long userId);

    Object findByMilestonesId(Long milestoneId);
}
