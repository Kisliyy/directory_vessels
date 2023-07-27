package com.smartgeosystems.directory_vessels.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public Caffeine<Object, Object> caffeine() {
        return Caffeine.newBuilder()
                .expireAfterWrite(2, TimeUnit.HOURS)
                .maximumSize(1000)
                .initialCapacity(10);
    }

    @Bean
    public CacheManager cacheManager() {
        var cacheManager = new CaffeineCacheManager();
        cacheManager.setCacheNames(List.of("vessels_imo", "vessels_mmsi", "vessel_users"));
        cacheManager.setCaffeine(caffeine());
        return cacheManager;
    }
}
