package com.roadmap.backendapi.repository;

import com.roadmap.backendapi.entity.Milestone;
import com.roadmap.backendapi.entity.PhoneNumber;
import com.roadmap.backendapi.entity.enums.MilestoneStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository interface for managing PhoneNumber entities.
 * This interface extends JpaRepository to provide CRUD operations
 * and custom query methods for PhoneNumber entities.
 *
 * @see com.roadmap.backendapi.entity.PhoneNumber
 * @see org.springframework.data.jpa.repository.JpaRepository
 */
public interface PhoneNumberRepository extends JpaRepository<PhoneNumber, Long> {
    Boolean existsByNumber(String number);
}
