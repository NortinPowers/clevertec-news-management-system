package by.clevertec.news.config;

import by.clevertec.news.cache.Cache;
import by.clevertec.news.cache.impl.LfuCache;
import by.clevertec.news.cache.impl.LruCache;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile(value = {"lfu-lru", "test"})
public class LfuLruCacheConfig {

    @Value("${cache.algorithm}")
    private String algorithm;

    @Value("${cache.max-collection-size}")
    private Integer maxCollectionSize;

    @Bean
    public Cache<Long, Object> cache() {
        if (maxCollectionSize == null) {
            maxCollectionSize = 30;
        }
        if ("lfu".equals(algorithm)) {
            return new LfuCache<>(maxCollectionSize);
        } else {
            return new LruCache<>(maxCollectionSize);
        }
    }
}
