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
    void createProgress_ShouldThrowExceptionWhenUserIdIsNull() {
        // Arrange
        CreateProgressRequest request = new CreateProgressRequest();
        request.setUserId(null);
        request.setMilestoneId(2L);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> progressService.createProgress(request));
        verify(progressRepository, never()).save(any());
    }

    @Test
    void createProgress_ShouldThrowExceptionWhenMilestoneIdIsNull() {
        // Arrange
        CreateProgressRequest request = new CreateProgressRequest();
        request.setUserId(1L);
        request.setMilestoneId(null);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> progressService.createProgress(request));
        verify(progressRepository, never()).save(any());
    }

    @Test
    void createProgress_ShouldHandleNullCompletedAt() {
        // Arrange
        Long userId = 1L;
        Long milestoneId = 2L;

        CreateProgressRequest request = new CreateProgressRequest();
        request.setUserId(userId);
        request.setMilestoneId(milestoneId);

        // Mock repository behavior
        when(progressRepository.existsByUserIdAndMilestoneId(userId, milestoneId)).thenReturn(false);

        Progress savedProgress = Progress.builder()
                .id(1L)
                .user(User.builder().id(userId).build())
                .milestone(Milestone.builder().id(milestoneId).build())
                .build();

        when(progressRepository.save(any(Progress.class))).thenReturn(savedProgress);

        ProgressDTO expectedDTO = ProgressDTO.builder()
                .id(1L)
                .build();

        when(progressMapper.toDTO(savedProgress)).thenReturn(expectedDTO);

        // Act
        ProgressDTO result = progressService.createProgress(request);

        // Assert
        assertNotNull(result);
        assertEquals(expectedDTO, result);

        ArgumentCaptor<Progress> progressCaptor = ArgumentCaptor.forClass(Progress.class);
        verify(progressRepository).save(progressCaptor.capture());

        Progress capturedProgress = progressCaptor.getValue();
        assertNull(capturedProgress.getCompleted_at());
    }

    @Test
    void createProgress_ShouldThrowExceptionWhenTimestampIsInvalid() {
        // Arrange
        Long userId = 1L;
        Long milestoneId = 2L;
        String invalidCompletedAt = "invalid-timestamp";

        CreateProgressRequest request = new CreateProgressRequest();
        request.setUserId(userId);
        request.setMilestoneId(milestoneId);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> progressService.createProgress(request));
        verify(progressRepository, never()).save(any());
    }
}