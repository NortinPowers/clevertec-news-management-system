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
@RequiredArgsConstructor
@Profile(value = {"lfu-lru", "test"})
public class NewsCacheableAspect {

    private final NewsRepository newsRepository;
    private final NewsMapper mapper;
    private final Cache<Long, Object> cache;

    /**
     * Обрабатывает метод NewsService.getById() с аннотацией {@link org.springframework.cache.annotation.Cacheable}.
     * Если результат с указанным идентификатором уже находится в кэше, возвращает его. В противном случае выполняет метод и сохраняет результат в кэше.
     *
     * @param joinPoint Точка соединения, представляющая вызов метода.
     * @return Результат выполнения метода или значение из кэша.
     * @throws CustomEntityNotFoundException Если объект с указанным идентификатором не найден.
     * @throws RuntimeException              Если возникает ошибка при выполнении метода.
     */
    @SuppressWarnings("checkstyle:IllegalCatch")
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

    /**
     * Кэширует созданный объект {@link News} после выполнения метода сохранения.
     * Используется при работе с lfu/lru - кэшем и с аннотацией {@link NewsCacheable}.
     *
     * @param id Идентификатор созданного объекта {@link News}.
     */
    @AfterReturning(pointcut = "@annotation(by.clevertec.news.proxy.NewsCacheable) && execution(* by.clevertec.news.service.NewsService.save(..))", returning = "id")
    public void cacheableCreate(Long id) {
        Optional<News> optionalNews = newsRepository.findById(id);
        optionalNews.ifPresent(news -> cache.put(id, mapper.toDto(news)));
    }

    /**
     * Удаляет объект из кэша после выполнения метода обновления {@link News}.
     * Метод необходимо пометить аннотацией {@link org.springframework.cache.annotation.Cacheable}.
     *
     * @param id Идентификатор объекта {@link News}, который был обновлен.
     */
    @AfterReturning(pointcut = "@annotation(org.springframework.cache.annotation.CacheEvict) && execution(* by.clevertec.news.service.NewsService.update(..)) && args(id)", argNames = "id")
    public void cacheableDelete(Long id) {
        cache.remove(id);
    }

    /**
     * Обновляет объект {@link News} в кэше после выполнения метода обновления.
     * Метод необходимо пометить аннотацией {@link org.springframework.cache.annotation.Cacheable}.
     *
     * @param id      Идентификатор объекта {@link News}, который был обновлен.
     * @param newsDto DTO с обновленными данными для объекта {@link News}.
     */
    @AfterReturning(pointcut = "@annotation(org.springframework.cache.annotation.CachePut) && execution(* by.clevertec.news.service.NewsService.update(..)) && args(id, newsDto)", argNames = "id, newsDto")
    public void cacheableUpdate(Long id, NewsRequestDto newsDto) {
        News news = newsRepository.findById(id).orElseThrow(() -> CustomEntityNotFoundException.of(News.class, id));
        cache.put(id, mapper.toDto(news));
    }
}
