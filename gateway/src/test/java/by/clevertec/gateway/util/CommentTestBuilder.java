package by.clevertec.gateway.util;

import static by.clevertec.gateway.util.TestConstant.AUTHOR_ID;
import static by.clevertec.gateway.util.TestConstant.AUTHOR_NAME;
import static by.clevertec.gateway.util.TestConstant.COMMENT_ID;
import static by.clevertec.gateway.util.TestConstant.COMMENT_TEXT;
import static by.clevertec.gateway.util.TestConstant.COMMENT_TIME;
import static by.clevertec.gateway.util.TestConstant.COMMENT_USERNAME;
import static by.clevertec.gateway.util.TestConstant.NEWS_ID;

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
        comment.setAuthor(AUTHOR_NAME);
        return comment;
    }
}
