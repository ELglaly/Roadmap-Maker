package com.roadmap.backendapi.interceptor;

import com.roadmap.backendapi.response.APIResponse;
import com.roadmap.backendapi.service.response.ResponseCacheService;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * Interceptor for caching API responses.
 * This interceptor automatically caches API responses for authenticated users.
 */
@ControllerAdvice
public class ResponseCacheInterceptor implements ResponseBodyAdvice<Object> {

    private final ResponseCacheService responseCacheService;

    /**
     * Constructor for ResponseCacheInterceptor.
     *
     * @param responseCacheService the ResponseCacheService used to cache responses
     */
    public ResponseCacheInterceptor(ResponseCacheService responseCacheService) {
        this.responseCacheService = responseCacheService;
    }

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // Only intercept responses that are not from the ResponseController
        return !returnType.getContainingClass().getSimpleName().equals("ResponseController");
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                 Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                 ServerHttpRequest request, ServerHttpResponse response) {
        
        // Only cache APIResponse objects
        if (body instanceof APIResponse apiResponse) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            
            // Only cache responses for authenticated users
            if (authentication != null && authentication.isAuthenticated() && 
                !authentication.getName().equals("anonymousUser")) {
                
                responseCacheService.cacheResponse(authentication.getName(), apiResponse);
            }
        }
        
        return body;
    }
}