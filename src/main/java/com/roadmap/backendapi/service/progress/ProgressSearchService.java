package com.roadmap.backendapi.service.progress;

import com.roadmap.backendapi.dto.ProgressDTO;

public interface ProgressSearchService {
    ProgressDTO getProgressById(Long progressId);
    ProgressDTO getProgressByUserId(Long userId);
    ProgressDTO getProgressByMilestoneId(Long milestoneId);
}
