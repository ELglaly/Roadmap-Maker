package com.roadmap.backendapi.repository;

import com.roadmap.backendapi.entity.Milestone;
import com.roadmap.backendapi.entity.PhoneNumber;
import com.roadmap.backendapi.entity.enums.MilestoneStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PhoneNumberRepository extends JpaRepository<PhoneNumber, Long> {
    Boolean existsByNumber(String number);
}
