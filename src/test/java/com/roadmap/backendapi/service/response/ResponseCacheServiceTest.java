package com.roadmap.backendapi.service.response;

import com.roadmap.backendapi.response.APIResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ResponseCacheServiceTest {

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private ValueOperations<String, Object> valueOperations;

    private ResponseCacheService responseCacheService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        responseCacheService = new ResponseCacheService(redisTemplate);
    }

    @Test
    void cacheResponse_shouldStoreResponseInRedis() {
        // Arrange
        String userId = "user123";
        APIResponse response = new APIResponse("Test message", "Test data");

        // Act
        responseCacheService.cacheResponse(userId, response);

        // Assert
        verify(valueOperations).set(eq("response:user123"), eq(response), eq(60L), eq(TimeUnit.MINUTES));
    }

    @Test
    void getPreviousResponse_whenResponseExists_shouldReturnResponse() {
        // Arrange
        String userId = "user123";
        APIResponse expectedResponse = new APIResponse("Test message", "Test data");
        when(valueOperations.get("response:user123")).thenReturn(expectedResponse);

        // Act
        APIResponse actualResponse = responseCacheService.getPreviousResponse(userId);

        // Assert
        assertEquals(expectedResponse, actualResponse);
        verify(valueOperations).get("response:user123");
    }

    @Test
    void getPreviousResponse_whenResponseDoesNotExist_shouldReturnNull() {
        // Arrange
        String userId = "user123";
        when(valueOperations.get("response:user123")).thenReturn(null);

        // Act
        APIResponse actualResponse = responseCacheService.getPreviousResponse(userId);

        // Assert
        assertNull(actualResponse);
        verify(valueOperations).get("response:user123");
    }
}