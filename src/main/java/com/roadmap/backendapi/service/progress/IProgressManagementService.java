package com.roadmap.backendapi.service.progress;

import com.roadmap.backendapi.dto.ProgressDTO;
import com.roadmap.backendapi.request.progress.CreateProgressRequest;
import com.roadmap.backendapi.request.progress.UpdateProgressRequest;

public interface IProgressManagementService {
    ProgressDTO updateProgress(UpdateProgressRequest request);
    ProgressDTO createProgress(CreateProgressRequest request);
    void deleteProgress(Long progressId);
}
