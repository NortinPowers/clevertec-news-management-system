package by.clevertec.comment.proxy;

import by.clevertec.comment.cache.Cache;
import by.clevertec.comment.domain.Comment;
import by.clevertec.comment.mapper.CommentMapper;
import by.clevertec.comment.repository.CommentRepository;
import by.clevertec.exception.CustomEntityNotFoundException;
import by.clevertec.request.CommentRequestDto;
import by.clevertec.response.CommentResponseDto;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class CommentCacheableAspect {

    private final CommentRepository commentRepository;
    private final CommentMapper mapper;
    private final Cache<Long, Object> cache;

    /**
     * Обрабатывает метод CommentService.getById() с аннотацией {@link org.springframework.cache.annotation.Cacheable}.
     * Если результат с указанным идентификатором уже находится в кэше, возвращает его. В противном случае выполняет метод и сохраняет результат в кэше.
     *
     * @param joinPoint Точка соединения, представляющая вызов метода.
     * @return Результат выполнения метода или значение из кэша.
     * @throws CustomEntityNotFoundException Если объект с указанным идентификатором не найден.
     * @throws RuntimeException              Если возникает ошибка при выполнении метода.
     */
    @SuppressWarnings("checkstyle:IllegalCatch")
    @Around("@annotation(org.springframework.cache.annotation.Cacheable) && execution(* by.clevertec.comment.service.CommentService.getById(..))")
    public Object cacheableGet(ProceedingJoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        Long id = (Long) args[0];
        if (cache.get(id) != null) {
            return cache.get(id);
        }
        CommentResponseDto result;
        try {
            result = (CommentResponseDto) joinPoint.proceed();
        } catch (CustomEntityNotFoundException e) {
            throw CustomEntityNotFoundException.of(Comment.class, id);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        cache.put(id, result);
        return result;
    }

    /**
     * Кэширует созданный объект {@link Comment} после выполнения метода сохранения.
     * Используется при работе с lfu/lru - кэшем и с аннотацией {@link CommentCacheable}.
     *
     * @param id Идентификатор созданного объекта {@link Comment}.
     */
    @AfterReturning(pointcut = "@annotation(CommentCacheable) && execution(* by.clevertec.comment.service.CommentService.save(..))", returning = "id")
    public void cacheableCreate(Long id) {
        Optional<Comment> optionalComment = commentRepository.findById(id);
        optionalComment.ifPresent(comment -> cache.put(id, mapper.toDto(comment)));
    }

    /**
     * Удаляет объект из кэша после выполнения метода обновления {@link Comment}.
     * Метод необходимо пометить аннотацией {@link org.springframework.cache.annotation.Cacheable}.
     *
     * @param id Идентификатор объекта {@link Comment}, который был обновлен.
     */
    @AfterReturning(pointcut = "@annotation(org.springframework.cache.annotation.CacheEvict) && execution(* by.clevertec.comment.service.CommentService.delete(..)) && args(id)", argNames = "id")
    public void cacheableDelete(Long id) {
        cache.remove(id);
    }

    /**
     * Обновляет объект {@link Comment} в кэше после выполнения метода обновления.
     * Метод необходимо пометить аннотацией {@link org.springframework.cache.annotation.Cacheable}.
     *
     * @param id         Идентификатор объекта {@link Comment}, который был обновлен.
     * @param commentDto DTO с обновленными данными для объекта {@link Comment}.
     */
    @AfterReturning(pointcut = "@annotation(org.springframework.cache.annotation.CachePut) && execution(* by.clevertec.comment.service.CommentService.update(..)) && args(id, commentDto)", argNames = "id, commentDto")
    public void cacheableUpdate(Long id, CommentRequestDto commentDto) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> CustomEntityNotFoundException.of(Comment.class, id));
        cache.put(id, mapper.toDto(comment));
    }
}
