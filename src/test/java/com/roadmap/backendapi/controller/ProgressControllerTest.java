package com.roadmap.backendapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.roadmap.backendapi.dto.ProgressDTO;
import com.roadmap.backendapi.request.progress.CreateProgressRequest;
import com.roadmap.backendapi.request.progress.UpdateProgressRequest;
import com.roadmap.backendapi.service.progress.ProgressService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Timestamp;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProgressController.class)
class ProgressControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private ProgressService progressService;

    @Autowired
    private ObjectMapper objectMapper;

    private ProgressDTO progressDTO;
    private CreateProgressRequest createRequest;
    private UpdateProgressRequest updateRequest;

    @BeforeEach
    void setUp() {
        progressDTO = ProgressDTO.builder()
                .id(1L)
                .completed_at(Timestamp.valueOf("2023-10-15 14:30:00"))
                .build();

        createRequest = new CreateProgressRequest();
        createRequest.setUserId(1L);
        createRequest.setMilestoneId(2L);

        updateRequest = new UpdateProgressRequest();
        updateRequest.setProgressId(1L);
        updateRequest.setCompleted_at(Timestamp.valueOf("2023-10-16 15:45:00"));
    }

    @Test
    void createProgress_ShouldReturnCreatedProgress() throws Exception {
        when(progressService.createProgress(any(CreateProgressRequest.class))).thenReturn(progressDTO);

        mockMvc.perform(post("/api/progress")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.id").value(progressDTO.getId()))
                .andExpect(jsonPath("$.message").value("Progress created successfully"));

        verify(progressService).createProgress(any(CreateProgressRequest.class));
    }

    @Test
    void updateProgress_ShouldReturnUpdatedProgress() throws Exception {
        when(progressService.updateProgress(any(UpdateProgressRequest.class))).thenReturn(progressDTO);

        mockMvc.perform(put("/api/progress")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(progressDTO.getId()))
                .andExpect(jsonPath("$.message").value("Progress updated successfully"));

        verify(progressService).updateProgress(any(UpdateProgressRequest.class));
    }

    @Test
    void deleteProgress_ShouldReturnSuccessMessage() throws Exception {
        Long progressId = 1L;

        mockMvc.perform(delete("/api/progress/{progressId}", progressId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Progress deleted successfully"));

        verify(progressService).deleteProgress(progressId);
    }

    @Test
    void getProgressById_ShouldReturnProgress() throws Exception {
        Long progressId = 1L;
        when(progressService.getProgressById(progressId)).thenReturn(progressDTO);

        mockMvc.perform(get("/api/progress/{progressId}", progressId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(progressDTO.getId()))
                .andExpect(jsonPath("$.message").value("Progress retrieved successfully"));

        verify(progressService).getProgressById(progressId);
    }

    @Test
    void getProgressByUserId_ShouldReturnProgress() throws Exception {
        Long userId = 1L;
        when(progressService.getProgressByUserId(userId)).thenReturn(progressDTO);

        mockMvc.perform(get("/api/progress/user/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(progressDTO.getId()))
                .andExpect(jsonPath("$.message").value("Progress retrieved successfully"));

        verify(progressService).getProgressByUserId(userId);
    }

    @Test
    void getProgressByMilestoneId_ShouldReturnProgress() throws Exception {
        Long milestoneId = 2L;
        when(progressService.getProgressByMilestoneId(milestoneId)).thenReturn(progressDTO);

        mockMvc.perform(get("/api/progress/milestone/{milestoneId}", milestoneId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(progressDTO.getId()))
                .andExpect(jsonPath("$.message").value("Progress retrieved successfully"));

        verify(progressService).getProgressByMilestoneId(milestoneId);
    }
}