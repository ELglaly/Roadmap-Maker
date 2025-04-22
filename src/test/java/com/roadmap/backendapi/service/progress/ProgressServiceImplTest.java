package com.roadmap.backendapi.service.progress;

import com.roadmap.backendapi.dto.ProgressDTO;
import com.roadmap.backendapi.entity.Milestone;
import com.roadmap.backendapi.entity.Progress;
import com.roadmap.backendapi.entity.User;
import com.roadmap.backendapi.exception.progress.ProgressAlreadyExistsException;
import com.roadmap.backendapi.mapper.ProgressMapper;
import com.roadmap.backendapi.repository.ProgressRepository;
import com.roadmap.backendapi.request.progress.CreateProgressRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProgressServiceImplTest {

    @Mock
    private ProgressRepository progressRepository;

    @Mock
    private ProgressMapper progressMapper;

    private ProgressServiceImpl progressService;

    @BeforeEach
    void setUp() {
        progressService = new ProgressServiceImpl(progressRepository, progressMapper);
    }

    @Test
    void createProgress_ShouldCreateProgressWithoutFetchingUser() {
        // Arrange
        Long userId = 1L;
        Long milestoneId = 2L;
        String completedAt = "2023-10-15 14:30:00";

        CreateProgressRequest request = new CreateProgressRequest();
        request.setUserId(userId);
        request.setMilestoneId(milestoneId);
        request.setCompleted_at(completedAt);

        // Mock repository behavior
        when(progressRepository.existsByUserIdAndMilestoneId(userId, milestoneId)).thenReturn(false);
        
        Progress savedProgress = Progress.builder()
                .id(1L)
                .user(User.builder().id(userId).build())
                .milestone(Milestone.builder().id(milestoneId).build())
                .completed_at(Timestamp.valueOf(completedAt))
                .build();
        
        when(progressRepository.save(any(Progress.class))).thenReturn(savedProgress);
        
        ProgressDTO expectedDTO = ProgressDTO.builder()
                .id(1L)
                .completed_at(Timestamp.valueOf(completedAt))
                .build();
        
        when(progressMapper.toDTO(savedProgress)).thenReturn(expectedDTO);

        // Act
        ProgressDTO result = progressService.createProgress(request);

        // Assert
        assertNotNull(result);
        assertEquals(expectedDTO, result);
        
        // Verify that we're creating a Progress with just the user ID, not fetching the full user
        ArgumentCaptor<Progress> progressCaptor = ArgumentCaptor.forClass(Progress.class);
        verify(progressRepository).save(progressCaptor.capture());
        
        Progress capturedProgress = progressCaptor.getValue();
        assertEquals(userId, capturedProgress.getUser().getId());
        assertEquals(milestoneId, capturedProgress.getMilestone().getId());
        assertEquals(Timestamp.valueOf(completedAt), capturedProgress.getCompleted_at());
        
        // Verify that we're not making any additional calls to fetch the user
        verify(progressRepository, never()).findByUserId(any());
    }

    @Test
    void createProgress_ShouldThrowExceptionWhenProgressAlreadyExists() {
        // Arrange
        Long userId = 1L;
        Long milestoneId = 2L;

        CreateProgressRequest request = new CreateProgressRequest();
        request.setUserId(userId);
        request.setMilestoneId(milestoneId);

        // Mock repository behavior
        when(progressRepository.existsByUserIdAndMilestoneId(userId, milestoneId)).thenReturn(true);

        // Act & Assert
        assertThrows(ProgressAlreadyExistsException.class, () -> progressService.createProgress(request));
        verify(progressRepository, never()).save(any());
    }

    @Test
    void createProgress_ShouldHandleInvalidTimestampFormat() {
        // Arrange
        Long userId = 1L;
        Long milestoneId = 2L;
        String invalidCompletedAt = "invalid-timestamp";

        CreateProgressRequest request = new CreateProgressRequest();
        request.setUserId(userId);
        request.setMilestoneId(milestoneId);
        request.setCompleted_at(invalidCompletedAt);

        // Mock repository behavior
        when(progressRepository.existsByUserIdAndMilestoneId(userId, milestoneId)).thenReturn(false);
        
        Progress savedProgress = Progress.builder()
                .id(1L)
                .user(User.builder().id(userId).build())
                .milestone(Milestone.builder().id(milestoneId).build())
                .completed_at(any(Timestamp.class))
                .build();
        
        when(progressRepository.save(any(Progress.class))).thenReturn(savedProgress);
        
        ProgressDTO expectedDTO = ProgressDTO.builder()
                .id(1L)
                .completed_at(any(Timestamp.class))
                .build();
        
        when(progressMapper.toDTO(savedProgress)).thenReturn(expectedDTO);

        // Act
        ProgressDTO result = progressService.createProgress(request);

        // Assert
        assertNotNull(result);
        
        // Verify that we're creating a Progress with a timestamp (even if the input was invalid)
        ArgumentCaptor<Progress> progressCaptor = ArgumentCaptor.forClass(Progress.class);
        verify(progressRepository).save(progressCaptor.capture());
        
        Progress capturedProgress = progressCaptor.getValue();
        assertNotNull(capturedProgress.getCompleted_at());
    }
}