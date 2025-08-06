package com.roadmap.backendapi.repository;

import com.roadmap.backendapi.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing User entities.
 * This interface extends JpaRepository to provide CRUD operations
 * and custom query methods for User entities.
 *
 * @see com.roadmap.backendapi.entity
 * @see org.springframework.data.jpa.repository.JpaRepository
 */
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    User findByUserContactEmail(String email);
    User findByUsernameOrUserContactEmail(String username, String email);
    Boolean existsByUsername(String username);
    Boolean existsByUserContactEmail(String email);
}
