package by.clevertec.news.proxy;

import by.clevertec.exception.CustomEntityNotFoundException;
import by.clevertec.news.cache.Cache;
import by.clevertec.news.domain.News;
import by.clevertec.news.mapper.NewsMapper;
import by.clevertec.news.repository.NewsRepository;
import by.clevertec.request.NewsRequestDto;
import by.clevertec.response.NewsResponseDto;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Profile(value = {"lfu-lru", "test"})
@RequiredArgsConstructor
public class NewsCacheableAspect {

    private final NewsRepository newsRepository;
    private final NewsMapper mapper;

    //    @Value("${cache.algorithm}")
//    private String algorithm;
//
//    @Value("${cache.max-collection-size}")
//    private Integer maxCollectionSize;
//    private Cache<Long, Object> cache = configureCache();
    private final Cache<Long, Object> cache;

    @SuppressWarnings("checkstyle:IllegalCatch")
//    @Around("@annotation(by.clevertec.news.proxy.NewsCacheable) && execution(* by.clevertec.news.service.NewsService.getById(..))")
    @Around("@annotation(org.springframework.cache.annotation.Cacheable) && execution(* by.clevertec.news.service.NewsService.getById(..))")
    public Object cacheableGet(ProceedingJoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        Long id = (Long) args[0];
        if (cache.get(id) != null) {
            return cache.get(id);
        }
        NewsResponseDto result;
        try {
            result = (NewsResponseDto) joinPoint.proceed();
        } catch (CustomEntityNotFoundException e) {
            throw CustomEntityNotFoundException.of(News.class, id);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        cache.put(id, result);
        return result;
    }

//    @Around("@annotation(newsCacheable) && execution(* by.clevertec.news.service.NewsService.getById(..))")
//    public Object cacheableGet(ProceedingJoinPoint joinPoint, NewsCacheable newsCacheable) {
//        String cacheKey = newsCacheable.key();
//
//        if (cache.get(cacheKey) != null) {
//            return cache.get(cacheKey);
//        }
//
//        NewsResponseDto result;
//        try {
//            result = (NewsResponseDto) joinPoint.proceed();
//        } catch (CustomEntityNotFoundException e) {
//            throw CustomEntityNotFoundException.of(News.class, Long.parseLong(cacheKey)); // Преобразуйте ключ обратно в Long, если необходимо
//        } catch (Throwable e) {
//            throw new RuntimeException(e);
//        }
//
//        cache.put(cacheKey, result);
//        return result;
//    }


    @AfterReturning(pointcut = "@annotation(by.clevertec.news.proxy.NewsCacheable) && execution(* by.clevertec.news.service.NewsService.save(..))", returning = "id")
//    @AfterReturning(pointcut = "@annotation(org.springframework.cache.annotation.Cacheable) && execution(* by.clevertec.news.service.NewsService.save(..))", returning = "id")
    public void cacheableCreate(Long id) {
        Optional<News> optionalNews = newsRepository.findById(id);
        optionalNews.ifPresent(news -> cache.put(id, mapper.toDto(news)));
    }

    //    @AfterReturning(pointcut = "@annotation(by.clevertec.news.proxy.NewsCacheable) && execution(* by.clevertec.news.service.NewsService.delete(..)) && args(id)", argNames = "id")
    @AfterReturning(pointcut = "@annotation(org.springframework.cache.annotation.CacheEvict) && execution(* by.clevertec.news.service.NewsService.update(..)) && args(id)", argNames = "id")

    public void cacheableDelete(Long id) {
        cache.remove(id);
    }

    //    @AfterReturning(pointcut = "@annotation(by.clevertec.news.proxy.NewsCacheable) && execution(* by.clevertec.news.service.NewsService.update(..)) && args(id, newsDto)", argNames = "id, newsDto")
    @AfterReturning(pointcut = "@annotation(org.springframework.cache.annotation.CachePut) && execution(* by.clevertec.news.service.NewsService.update(..)) && args(id, newsDto)", argNames = "id, newsDto")
    public void cacheableUpdate(Long id, NewsRequestDto newsDto) {
        News news = newsRepository.findById(id).orElseThrow(() -> CustomEntityNotFoundException.of(News.class, id));
        cache.put(id, mapper.toDto(news));
    }

//    private Cache<Long, Object> configureCache() {
//        if (cache == null) {
//            synchronized (this) {
//                if (maxCollectionSize == null) {
//                    maxCollectionSize = 30;
//                }
//                if ("lfu".equals(algorithm)) {
//                    cache = new LfuCache<>(maxCollectionSize);
//                } else {
//                    cache = new LruCache<>(maxCollectionSize);
//                }
//            }
//        }
//        return cache;
//    }
}

