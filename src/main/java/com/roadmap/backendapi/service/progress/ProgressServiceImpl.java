package com.roadmap.backendapi.service.progress;

import com.roadmap.backendapi.dto.ProgressDTO;
import com.roadmap.backendapi.exception.progress.ProgressNotFoundException;
import com.roadmap.backendapi.mapper.ProgressMapper;
import com.roadmap.backendapi.repository.ProgressRepository;
import com.roadmap.backendapi.request.progress.CreateProgressRequest;
import com.roadmap.backendapi.request.progress.UpdateProgressRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProgressServiceImpl implements ProgressService {

    private final ProgressRepository progressRepository;
    private final ProgressMapper progressMapper;

    public ProgressServiceImpl(ProgressRepository progressRepository, ProgressMapper progressMapper) {
        this.progressRepository = progressRepository;
        this.progressMapper = progressMapper;
    }


    @Override
    public ProgressDTO updateProgress(UpdateProgressRequest request) {
        return null;
    }

    @Override
    public ProgressDTO createProgress(CreateProgressRequest request) {
        return null;
    }

    @Override
    public void deleteProgress(Long progressId) {
        if (progressRepository.existsById(progressId))
            progressRepository.deleteById(progressId);
        else
            throw new ProgressNotFoundException();

    }

    @Override
    public ProgressDTO getProgressById(Long progressId) {
        return progressRepository.findById(progressId)
                .map(progressMapper::toDTO)
                .orElseThrow(ProgressNotFoundException::new);
    }

    @Override
    public ProgressDTO getProgressByUserId(Long userId) {
        return Optional.ofNullable(progressRepository.findByUserId(userId))
                .map(progressMapper::toDTO)
                .orElseThrow(ProgressNotFoundException::new);
    }

    @Override
    public ProgressDTO getProgressByMilestoneId(Long milestoneId) {
        return Optional.ofNullable(progressRepository.findByMilestoneId(milestoneId))
                .map(progressMapper::toDTO)
                .orElseThrow(ProgressNotFoundException::new);
    }
}
