package com.roadmap.backendapi.repository;

import com.roadmap.backendapi.entity.Roadmap;
import com.roadmap.backendapi.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Repository interface for managing Roadmap entities.
 * This interface extends JpaRepository to provide CRUD operations
 * and custom query methods for Roadmap entities.
 *
 * @see com.roadmap.backendapi.entity.Roadmap
 * @see org.springframework.data.jpa.repository.JpaRepository
 */
public interface RoadmapRepository extends JpaRepository<Roadmap, Long> {
    Roadmap findByTitle(String title);
    Boolean existsByTitle(String title);
    Roadmap findByUser(User user);
    Boolean existsByUserId(Long userId);

    List<Roadmap> findByUserId(Long userId);


    //@Query("SELECT r FROM Roadmap r WHERE r.title LIKE %?1%")
    List<Roadmap> findByTitleContaining(String title);
}
