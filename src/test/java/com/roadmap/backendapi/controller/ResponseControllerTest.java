package com.roadmap.backendapi.controller;

import com.roadmap.backendapi.response.APIResponse;
import com.roadmap.backendapi.service.response.ResponseCacheService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

class ResponseControllerTest {

    @Mock
    private ResponseCacheService responseCacheService;

    @InjectMocks
    private ResponseController responseController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getPreviousResponse_whenResponseExists_shouldReturnOkWithResponse() {
        // Arrange
        String userId = "user123";
        APIResponse expectedResponse = new APIResponse("Test message", "Test data");
        when(responseCacheService.getPreviousResponse(userId)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<APIResponse> responseEntity = responseController.getPreviousResponse(userId);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse, responseEntity.getBody());
        verify(responseCacheService).getPreviousResponse(userId);
    }

    @Test
    void getPreviousResponse_whenResponseDoesNotExist_shouldReturnNotFound() {
        // Arrange
        String userId = "user123";
        when(responseCacheService.getPreviousResponse(userId)).thenReturn(null);

        // Act
        ResponseEntity<APIResponse> responseEntity = responseController.getPreviousResponse(userId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("No previous response found", responseEntity.getBody().getMessage());
        assertNull(responseEntity.getBody().getData());
        verify(responseCacheService).getPreviousResponse(userId);
    }
}