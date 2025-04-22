package com.roadmap.backendapi.interceptor;

import com.roadmap.backendapi.controller.ResponseController;
import com.roadmap.backendapi.response.APIResponse;
import com.roadmap.backendapi.service.response.ResponseCacheService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class ResponseCacheInterceptorTest {

    @Mock
    private ResponseCacheService responseCacheService;

    @Mock
    private ServerHttpRequest request;

    @Mock
    private ServerHttpResponse response;

    @Mock
    private HttpMessageConverter<?> converter;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private ResponseCacheInterceptor interceptor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void supports_whenControllerIsResponseController_shouldReturnFalse() throws NoSuchMethodException {
        // Arrange
        Method method = ResponseController.class.getMethod("getPreviousResponse", String.class);
        MethodParameter methodParameter = new MethodParameter(method, -1);

        // Act
        boolean result = interceptor.supports(methodParameter, converter.getClass());

        // Assert
        assertFalse(result);
    }

    @Test
    void supports_whenControllerIsNotResponseController_shouldReturnTrue() throws NoSuchMethodException {
        // Arrange
        Method method = Object.class.getMethod("toString");
        MethodParameter methodParameter = new MethodParameter(method, -1);

        // Act
        boolean result = interceptor.supports(methodParameter, converter.getClass());

        // Assert
        assertTrue(result);
    }

    @Test
    void beforeBodyWrite_whenBodyIsAPIResponseAndUserIsAuthenticated_shouldCacheResponse() {
        // Arrange
        APIResponse apiResponse = new APIResponse("Test message", "Test data");
        Authentication authentication = new UsernamePasswordAuthenticationToken("user123", "password");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);

        // Act
        Object result = interceptor.beforeBodyWrite(apiResponse, null, MediaType.APPLICATION_JSON, 
                                                  null, request, response);

        // Assert
        assertEquals(apiResponse, result);
        verify(responseCacheService).cacheResponse("user123", apiResponse);
    }

    @Test
    void beforeBodyWrite_whenBodyIsNotAPIResponse_shouldNotCacheResponse() {
        // Arrange
        String body = "Not an APIResponse";
        Authentication authentication = new UsernamePasswordAuthenticationToken("user123", "password");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);

        // Act
        Object result = interceptor.beforeBodyWrite(body, null, MediaType.APPLICATION_JSON, 
                                                  null, request, response);

        // Assert
        assertEquals(body, result);
        verify(responseCacheService, never()).cacheResponse(anyString(), any(APIResponse.class));
    }

    @Test
    void beforeBodyWrite_whenUserIsNotAuthenticated_shouldNotCacheResponse() {
        // Arrange
        APIResponse apiResponse = new APIResponse("Test message", "Test data");
        Authentication authentication = new UsernamePasswordAuthenticationToken("anonymousUser", "");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn("anonymousUser");

        // Act
        Object result = interceptor.beforeBodyWrite(apiResponse, null, MediaType.APPLICATION_JSON, 
                                                  null, request, response);

        // Assert
        assertEquals(apiResponse, result);
        verify(responseCacheService, never()).cacheResponse(anyString(), any(APIResponse.class));
    }
    
    private void assertEquals(Object expected, Object actual) {
        if (expected == null && actual == null) {
            return;
        }
        if (expected != null && expected.equals(actual)) {
            return;
        }
        throw new AssertionError("Expected " + expected + " but got " + actual);
    }
}