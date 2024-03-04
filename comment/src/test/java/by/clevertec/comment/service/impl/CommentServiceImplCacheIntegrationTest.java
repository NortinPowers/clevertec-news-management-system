package by.clevertec.comment.service.impl;

import static by.clevertec.comment.util.TestConstant.NEWS_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import by.clevertec.comment.cache.Cache;
import by.clevertec.comment.domain.Comment;
import by.clevertec.comment.mapper.CommentMapper;
import by.clevertec.comment.proxy.CommentCacheableAspect;
import by.clevertec.comment.repository.CommentRepository;
import by.clevertec.comment.util.CommentTestBuilder;
import by.clevertec.response.CommentResponseDto;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@RequiredArgsConstructor
public class CommentServiceImplCacheIntegrationTest {

    private final CommentCacheableAspect cacheableAspect;
    private final CommentServiceImpl commentService;

    @MockBean
    private CommentRepository commentRepository;

    @MockBean
    private CommentMapper commentMapper;

    @MockBean
    private Cache<Long, Object> cache;

    @MockBean
    private ProceedingJoinPoint joinPoint;

    @Test
    void getShouldReturnCommentResponseDtoFromAop_whenCommentResponseDtoWithCurrentIdIsNotInCacheButIsInRepository() throws Throwable {
        CommentResponseDto expected = CommentTestBuilder.builder()
                .build()
                .buildCommentResponseDto();

        lenient()
                .when(joinPoint.getArgs())
                .thenReturn(new Object[]{NEWS_ID});
        lenient()
                .when(cache.get(NEWS_ID))
                .thenReturn(null);
        when(joinPoint.proceed())
                .thenReturn(expected);

        CommentResponseDto actual = (CommentResponseDto) cacheableAspect.cacheableGet(joinPoint);

        assertThat(actual)
                .hasFieldOrPropertyWithValue(Comment.Fields.time, expected.getTime())
                .hasFieldOrPropertyWithValue(Comment.Fields.text, expected.getText())
                .hasFieldOrPropertyWithValue(Comment.Fields.username, expected.getUsername())
                .hasFieldOrPropertyWithValue("newsId", expected.getNewsId())
                .hasFieldOrPropertyWithValue("author", expected.getAuthor());
    }

    @Test
    void getShouldReturnCommentResponseDtoFromRepository_whenCommentWithCurrentUuidExist() {
        CommentResponseDto expected = CommentTestBuilder.builder()
                .build()
                .buildCommentResponseDto();
        Comment comment = CommentTestBuilder.builder()
                .build()
                .buildComment();

        when(commentRepository.findById(NEWS_ID))
                .thenReturn(Optional.of(comment));
        when(commentMapper.toDto(comment))
                .thenReturn(expected);

        CommentResponseDto actual = commentService.getById(NEWS_ID);

        assertThat(actual)
                .hasFieldOrPropertyWithValue(Comment.Fields.time, expected.getTime())
                .hasFieldOrPropertyWithValue(Comment.Fields.text, expected.getText())
                .hasFieldOrPropertyWithValue(Comment.Fields.username, expected.getUsername())
                .hasFieldOrPropertyWithValue("newsId", expected.getNewsId())
                .hasFieldOrPropertyWithValue("author", expected.getAuthor());
    }
}
