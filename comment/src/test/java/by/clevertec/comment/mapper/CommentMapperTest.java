package by.clevertec.comment.mapper;

import by.clevertec.comment.domain.Comment;
import by.clevertec.comment.mapper.CommentMapper;
import by.clevertec.comment.util.CommentTestBuilder;
import by.clevertec.request.CommentPathRequestDto;
import by.clevertec.request.CommentRequestDto;
import by.clevertec.response.CommentResponseDto;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
@RequiredArgsConstructor
class CommentMapperTest{

    private final CommentMapper commentMapper;

    @Test
    void toDtoShouldReturnCommentResponseDto_whenCommentPassed() {
        Comment comment = CommentTestBuilder.builder()
                .build()
                .buildComment();
        CommentResponseDto expected = CommentTestBuilder.builder()
                .build()
                .buildCommentResponseDto();

        CommentResponseDto actual = commentMapper.toDto(comment);

        assertThat(actual)
                .hasFieldOrPropertyWithValue(Comment.Fields.time, expected.getTime())
                .hasFieldOrPropertyWithValue(Comment.Fields.text, expected.getText())
                .hasFieldOrPropertyWithValue(Comment.Fields.username, expected.getUsername())
                .hasFieldOrPropertyWithValue("newsId", expected.getNewsId())
                .hasFieldOrPropertyWithValue("author", expected.getAuthor());
    }

    @Test
    void commentRequestDtoToDomainShouldReturnComment_whenCommentRequestDtoPassed() {
        CommentRequestDto commentRequestDto = CommentTestBuilder.builder()
                .build()
                .buildCommentRequestDto();
        Comment expected = CommentTestBuilder.builder()
                .build()
                .buildComment();

        Comment actual = commentMapper.toDomain(commentRequestDto);

        assertThat(actual)
                .hasFieldOrPropertyWithValue(Comment.Fields.text, expected.getText())
                .hasFieldOrPropertyWithValue(Comment.Fields.username, expected.getUsername())
                .hasFieldOrPropertyWithValue(Comment.Fields.username, expected.getUsername());
        assertEquals(actual.getNews().getId(), expected.getNews().getId());
    }

    @Test
    void commentPathRequestDtoToDomainShouldReturnComment_whenCommentPathRequestDtoPassed() {
        CommentPathRequestDto commentPathRequestDto = CommentTestBuilder.builder()
                .build()
                .buildCommentPathRequestDto();
        Comment expected = CommentTestBuilder.builder()
                .build()
                .buildComment();

        Comment actual = commentMapper.toDomain(commentPathRequestDto);

        assertThat(actual)
                .hasFieldOrPropertyWithValue(Comment.Fields.text, expected.getText())
                .hasFieldOrPropertyWithValue(Comment.Fields.username, expected.getUsername());
        assertEquals(actual.getNews().getId(), expected.getNews().getId());

    }

    @Test
    void mergeShouldReturnUpdatedComment_whenCommentForUpdatePassed() {
        String text = "new news text";
        Comment comment = CommentTestBuilder.builder()
                .build()
                .buildComment();
        Comment updated = CommentTestBuilder.builder()
                .withText(text)
                .build()
                .buildComment();
        Comment expected = CommentTestBuilder.builder()
                .withText(text)
                .build()
                .buildComment();

        Comment actual = commentMapper.merge(comment, updated);

        assertThat(actual)
                .hasFieldOrPropertyWithValue(Comment.Fields.id, expected.getId())
                .hasFieldOrPropertyWithValue(Comment.Fields.time, expected.getTime())
                .hasFieldOrPropertyWithValue(Comment.Fields.text, expected.getText())
                .hasFieldOrPropertyWithValue(Comment.Fields.username, expected.getUsername())
                .hasFieldOrPropertyWithValue(Comment.Fields.news, expected.getNews())
                .hasFieldOrPropertyWithValue(Comment.Fields.author, expected.getAuthor());
    }
}
