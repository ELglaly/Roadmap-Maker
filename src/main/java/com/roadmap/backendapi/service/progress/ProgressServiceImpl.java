package com.roadmap.backendapi.service.progress;

import com.roadmap.backendapi.dto.ProgressDTO;
import com.roadmap.backendapi.entity.Milestone;
import com.roadmap.backendapi.entity.Progress;
import com.roadmap.backendapi.exception.progress.ProgressAlreadyExistsException;
import com.roadmap.backendapi.exception.progress.ProgressNotFoundException;
import com.roadmap.backendapi.mapper.ProgressMapper;
import com.roadmap.backendapi.repository.ProgressRepository;
import com.roadmap.backendapi.request.progress.CreateProgressRequest;
import com.roadmap.backendapi.request.progress.UpdateProgressRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ProgressServiceImpl implements ProgressService {

    private final ProgressRepository progressRepository;
    private final ProgressMapper progressMapper;

    public ProgressServiceImpl(ProgressRepository progressRepository, ProgressMapper progressMapper) {
        this.progressRepository = progressRepository;
        this.progressMapper = progressMapper;
    }


    /**
     * Updates the progress of a milestone.
     *
     * @param request the request containing the progress update details
     * @return the updated ProgressDTO
     * @throws ProgressNotFoundException if the progress with the given ID does not exist
     * @throws ProgressAlreadyExistsException if the progress percentage is invalid
     */
    @Override
    public ProgressDTO updateProgress(UpdateProgressRequest request) {
        Progress progress = progressRepository.findById(request.getProgressId())
                .orElseThrow(ProgressNotFoundException::new);
        if (request.getPercentage().compareTo(BigDecimal.ZERO) < 0 ||
                request.getPercentage().compareTo(new BigDecimal("100")) > 0) {
            throw new ProgressAlreadyExistsException();
        }
        else {
            markAsCompleted(progress, request);
        }
        return progressMapper.toDTO(progressRepository.save(progress));
    }



    /**
     * Creates a new progress entry for a milestone.
     *
     * @param request the request containing the milestone ID
     * @return the created ProgressDTO
     * @throws ProgressAlreadyExistsException if a progress entry for the milestone already exists
     */
    @Override
    public ProgressDTO createProgress(CreateProgressRequest request) {
        if (progressRepository.existsByMilestoneId(request.getMilestoneId())) {
            throw new ProgressAlreadyExistsException();
        }
            // Create a new Progress entity without fetching the User from the database
            Progress progress = Progress.builder()
                .milestone(Milestone.builder().id(request.getMilestoneId()).build())
                .build();

            // Save the progress and return the DTO
            return progressMapper.toDTO(progressRepository.save(progress));
    }


    /**
     * Retrieves the progress percentage as an integer for a given progress ID.
     * @param progressId
     * @return
     */
    @Override
    public int getProgressAsInteger(Long progressId) {
      return progressRepository.findById(progressId)
                .map(progress -> progress.getProgressPercentage().intValue())
              .orElseThrow(ProgressNotFoundException::new);
    }


    /**
     * Deletes a progress entry by its ID.
     *
     * @param progressId the ID of the progress entry to delete
     * @throws ProgressNotFoundException if the progress with the given ID does not exist
     */
    @Override
    public void deleteProgress(Long progressId) {
        if (progressRepository.existsById(progressId))
            progressRepository.deleteById(progressId);
        else
            throw new ProgressNotFoundException();

    }


    /**
     * Marks the progress as completed based on the provided request.
     *
     * @param progress the Progress entity to be updated
     * @param request  the request containing the percentage to update
     */
    @Override
    public void markAsCompleted(Progress progress, UpdateProgressRequest request) {
        progress.setProgressPercentage(request.getPercentage());
        if (progress.getProgressPercentage() .compareTo(new BigDecimal("100.00")) >= 0) {
            progress.setCompletedAt(LocalDateTime.now());
        } else {
            progress.setCompletedAt(null);
        }
    }


    /**
     * Retrieves the progress by its ID.
     *
     * @param progressId the ID of the progress entry
     * @return ProgressDTO containing the progress details
     * @throws ProgressNotFoundException if the progress with the given ID does not exist
     */
    @Override
    public ProgressDTO getProgressById(Long progressId) {
        return progressRepository.findById(progressId)
                .map(progressMapper::toDTO)
                .orElseThrow(ProgressNotFoundException::new);
    }



    /**
     * Retrieves the progress by its milestone ID.
     *
     * @param milestoneId the ID of the milestone
     * @return ProgressDTO containing the progress details
     * @throws ProgressNotFoundException if the progress for the given milestone does not exist
     */
    @Override
    public ProgressDTO getProgressByMilestoneId(Long milestoneId) {
        return Optional.ofNullable(progressRepository.findByMilestoneId(milestoneId))
                .map(progressMapper::toDTO)
                .orElseThrow(ProgressNotFoundException::new);
    }

    /**
     * Checks if the progress with the given ID is completed.
     *
     * @param progressId the ID of the progress entry
     * @return true if the progress is completed, false otherwise
     * @throws ProgressNotFoundException if the progress with the given ID does not exist
     */
    @Override
    public boolean isCompleted(Long progressId) {
        return progressRepository.findById(progressId)
                .map(progress -> progress.getCompletedAt() != null)
                .orElseThrow(ProgressNotFoundException::new);
    }
}