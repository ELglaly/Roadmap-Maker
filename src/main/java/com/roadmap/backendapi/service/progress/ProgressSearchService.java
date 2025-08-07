package com.roadmap.backendapi.service.progress;

import com.roadmap.backendapi.dto.ProgressDTO;

public interface ProgressSearchService {
    ProgressDTO getProgressById(Long progressId);
    boolean isCompleted(Long progressId);

}
