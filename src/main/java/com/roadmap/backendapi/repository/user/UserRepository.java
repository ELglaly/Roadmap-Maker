package com.roadmap.backendapi.repository.user;

import com.roadmap.backendapi.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Repository interface for managing User entities.
 * This interface extends JpaRepository to provide CRUD operations
 * and custom query methods for User entities.
 *
 * @see com.roadmap.backendapi.entity
 * @see org.springframework.data.jpa.repository.JpaRepository
 */
public interface UserRepository extends JpaRepository<User, Long> {

    <T> T findByUserContactEmail(String email, Class<T> type);
    <T> T findByUsernameOrUserContactEmail(String username, String email, Class<T> type);
    Boolean existsByUsername(String username);
    Boolean existsByUserContactEmail(String email);
    <T> T findByUsername(String username, Class<T> type);
    <T> T findById(Long id, Class<T> type);

}
