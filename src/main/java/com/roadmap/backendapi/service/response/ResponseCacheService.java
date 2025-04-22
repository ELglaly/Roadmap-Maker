package com.roadmap.backendapi.service.response;

import com.roadmap.backendapi.response.APIResponse;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * Service for caching and retrieving API responses.
 * This service uses Redis to store and retrieve previous API responses.
 */
@Service
public class ResponseCacheService {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String RESPONSE_CACHE_PREFIX = "response:";
    private static final long CACHE_TTL_MINUTES = 60;

    /**
     * Constructor for ResponseCacheService.
     *
     * @param redisTemplate the RedisTemplate used to interact with Redis
     */
    public ResponseCacheService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * Caches an API response for a specific user.
     *
     * @param userId the ID of the user
     * @param response the API response to cache
     */
    public void cacheResponse(String userId, APIResponse response) {
        String key = generateCacheKey(userId);
        redisTemplate.opsForValue().set(key, response, CACHE_TTL_MINUTES, TimeUnit.MINUTES);
    }

    /**
     * Retrieves the previous API response for a specific user.
     *
     * @param userId the ID of the user
     * @return the previous API response, or null if no previous response exists
     */
    public APIResponse getPreviousResponse(String userId) {
        String key = generateCacheKey(userId);
        return (APIResponse) redisTemplate.opsForValue().get(key);
    }

    /**
     * Generates a cache key for a specific user.
     *
     * @param userId the ID of the user
     * @return the cache key
     */
    private String generateCacheKey(String userId) {
        return RESPONSE_CACHE_PREFIX + userId;
    }
}