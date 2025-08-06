package com.roadmap.backendapi.service.progress;

import com.roadmap.backendapi.dto.ProgressDTO;
import com.roadmap.backendapi.entity.Progress;
import com.roadmap.backendapi.request.progress.CreateProgressRequest;
import com.roadmap.backendapi.request.progress.UpdateProgressRequest;

import java.math.BigDecimal;

public interface ProgressManagementService {
    ProgressDTO updateProgress(UpdateProgressRequest request);
    ProgressDTO createProgress(CreateProgressRequest request);
    int getProgressAsInteger(Long progressId);
    void deleteProgress(Long progressId);
    void markAsCompleted(Progress progress, UpdateProgressRequest request);
}
