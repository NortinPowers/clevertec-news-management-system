package by.clevertec.comment.util;

import static by.clevertec.comment.util.TestConstant.AUTHOR_ID;
import static by.clevertec.comment.util.TestConstant.AUTHOR_NAME;
import static by.clevertec.comment.util.TestConstant.COMMENT_ID;
import static by.clevertec.comment.util.TestConstant.COMMENT_TEXT;
import static by.clevertec.comment.util.TestConstant.COMMENT_TIME;
import static by.clevertec.comment.util.TestConstant.COMMENT_USERNAME;
import static by.clevertec.comment.util.TestConstant.NEWS_ID;

import by.clevertec.comment.domain.Author;
import by.clevertec.comment.domain.Comment;
import by.clevertec.comment.domain.News;
import by.clevertec.request.CommentAndNamePathRequestDto;
import by.clevertec.request.CommentAndNameRequestDto;
import by.clevertec.request.CommentPathRequestDto;
import by.clevertec.request.CommentRequestDto;
import by.clevertec.response.CommentResponseDto;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(setterPrefix = "with")
public class CommentTestBuilder {

    @Builder.Default
    private Long id = COMMENT_ID;

    @Builder.Default
    private LocalDateTime time = COMMENT_TIME;

    @Builder.Default
    private String text = COMMENT_TEXT;

    @Builder.Default
    private String username = COMMENT_USERNAME;

    @Builder.Default
    private Long newsId = NEWS_ID;

    @Builder.Default
    private Long authorId = AUTHOR_ID;

    @Builder.Default
    private Author author = new Author(AUTHOR_NAME);

    @Builder.Default
    private News news = NewsTestBuilder.builder()
            .build()
            .buildNews();

    public Comment buildComment() {
        Comment comment = new Comment();
        comment.setId(id);
        comment.setTime(time);
        comment.setText(text);
        comment.setUsername(username);
        comment.setNews(news);
        comment.setAuthor(author);
        return comment;
    }

    public CommentPathRequestDto buildCommentPathRequestDto() {
        CommentPathRequestDto comment = new CommentPathRequestDto();
        comment.setText(text);
        comment.setUsername(username);
        comment.setNewsId(NEWS_ID);
        return comment;
    }

    public CommentRequestDto buildCommentRequestDto() {
        CommentRequestDto comment = new CommentRequestDto();
        comment.setText(text);
        comment.setUsername(username);
        comment.setNewsId(NEWS_ID);
        return comment;
    }

    public CommentResponseDto buildCommentResponseDto() {
        CommentResponseDto comment = new CommentResponseDto();
        comment.setTime(time.toString());
        comment.setText(text);
        comment.setUsername(username);
        comment.setNewsId(NEWS_ID);
        comment.setAuthor(author.getName());
        return comment;
    }

    public CommentAndNamePathRequestDto buildCommentAndNamePathRequestDto() {
        CommentAndNamePathRequestDto requestDto = new CommentAndNamePathRequestDto();
        requestDto.setName(AUTHOR_NAME);
        CommentPathRequestDto comment = new CommentPathRequestDto();
        comment.setText(text);
        comment.setUsername(AUTHOR_NAME);
        comment.setNewsId(NEWS_ID);
        requestDto.setRequestDto(comment);
        return requestDto;
    }

    public CommentAndNameRequestDto buildCommentAndNameRequestDto() {
        CommentAndNameRequestDto requestDto = new CommentAndNameRequestDto();
        requestDto.setName(AUTHOR_NAME);
        CommentRequestDto comment = new CommentRequestDto();
        comment.setText(text);
        comment.setUsername(AUTHOR_NAME);
        comment.setNewsId(NEWS_ID);
        requestDto.setRequestDto(comment);
        return requestDto;
    }
}
