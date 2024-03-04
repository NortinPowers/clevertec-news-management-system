package by.clevertec.comment.proxy;

import by.clevertec.comment.cache.Cache;
import by.clevertec.comment.cache.impl.LfuCache;
import by.clevertec.comment.cache.impl.LruCache;
import by.clevertec.comment.domain.Comment;
import by.clevertec.comment.mapper.CommentMapper;
import by.clevertec.comment.repository.CommentRepository;
import by.clevertec.exception.CustomEntityNotFoundException;
import by.clevertec.request.CommentRequestDto;
import by.clevertec.response.CommentResponseDto;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Aspect
@Component
@RequiredArgsConstructor
public class CommentCacheableAspect {

    private final CommentRepository commentRepository;
    private final CommentMapper mapper;
    private final Cache<Long, Object> cache;

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

    @AfterReturning(pointcut = "@annotation(CommentCacheable) && execution(* by.clevertec.comment.service.CommentService.save(..))", returning = "id")
//    @AfterReturning(pointcut = "@annotation(org.springframework.cache.annotation.Cacheable) && execution(* by.clevertec.comment.service.CommentService.save(..))", returning = "id")
    public void cacheableCreate(Long id) {
        Optional<Comment> optionalComment = commentRepository.findById(id);
        optionalComment.ifPresent(comment -> cache.put(id, mapper.toDto(comment)));
    }

    @AfterReturning(pointcut = "@annotation(org.springframework.cache.annotation.CacheEvict) && execution(* by.clevertec.comment.service.CommentService.delete(..)) && args(id)", argNames = "id")
    public void cacheableDelete(Long id) {
        cache.remove(id);
    }

    @AfterReturning(pointcut = "@annotation(org.springframework.cache.annotation.CachePut) && execution(* by.clevertec.comment.service.CommentService.update(..)) && args(id, commentDto)", argNames = "id, commentDto")
    public void cacheableUpdate(Long id, CommentRequestDto commentDto) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> CustomEntityNotFoundException.of(Comment.class, id));
        cache.put(id, mapper.toDto(comment));
    }
}

