package com.roadmap.backendapi.service.progress;

import com.roadmap.backendapi.dto.ProgressDTO;
import com.roadmap.backendapi.entity.Milestone;
import com.roadmap.backendapi.entity.Progress;
import com.roadmap.backendapi.entity.User;
import com.roadmap.backendapi.exception.progress.ProgressAlreadyExistsException;
import com.roadmap.backendapi.exception.progress.ProgressNotFoundException;
import com.roadmap.backendapi.mapper.ProgressMapper;
import com.roadmap.backendapi.repository.ProgressRepository;
import com.roadmap.backendapi.request.progress.CreateProgressRequest;
import com.roadmap.backendapi.request.progress.UpdateProgressRequest;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
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
        return progressRepository.findById(request.getProgressId())
                .map(progress -> {
                    if (request.getCompleted_at() != null)
                        progress.setCompleted_at(request.getCompleted_at());
                    
                    return progressMapper.toDTO(progressRepository.save(progress));
                })
                .orElseThrow(ProgressNotFoundException::new);
    }

    @Override
    public ProgressDTO createProgress(CreateProgressRequest request) {
        if (progressRepository.existsByUserIdAndMilestoneId(request.getUserId(), request.getMilestoneId()))
            throw new ProgressAlreadyExistsException();
        else {
            // Create a new Progress entity without fetching the User from the database
            Progress progress = Progress.builder()
                .user(User.builder().id(request.getUserId()).build()) // Create a User reference with just the ID
                .milestone(Milestone.builder().id(request.getMilestoneId()).build()) // Create a Milestone reference with just the ID
                .build();
            
            // Set completed_at if provided, otherwise it will use the default CURRENT_TIMESTAMP
            if (request.getCompleted_at() != null) {
                try {
                    progress.setCompleted_at(Timestamp.valueOf(request.getCompleted_at()));
                } catch (IllegalArgumentException e) {
                    // If the timestamp format is invalid, use current timestamp
                    progress.setCompleted_at(new Timestamp(System.currentTimeMillis()));
                }
            }
            
            // Save the progress and return the DTO
            return progressMapper.toDTO(progressRepository.save(progress));
        }
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