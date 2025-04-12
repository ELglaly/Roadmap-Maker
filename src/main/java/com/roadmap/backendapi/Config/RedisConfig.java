package com.roadmap.backendapi.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import java.time.Duration;

/**
 * Configuration class for Redis Cache.
 * This class is responsible for creating a RedisCacheManager bean with a default cache configuration.
 * The default cache configuration includes a time-to-live (TTL) of 60 minutes and serialization settings.
 *
 * @see RedisCacheManager
 * @see RedisCacheConfiguration
 */
@Configuration
public class RedisConfig {

    /**
     * Creates a RedisCacheManager bean with a default cache configuration.
     *
     * @param connectionFactory the RedisConnectionFactory used to create the RedisCacheManager
     * @return a RedisCacheManager instance with the specified default cache configuration
     */
    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        RedisCacheConfiguration redisCacheConfiguration =  RedisCacheConfiguration
                .defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(60))
                .disableCachingNullValues()
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new GenericJackson2JsonRedisSerializer()));

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(redisCacheConfiguration)
                .build();
    }
}
