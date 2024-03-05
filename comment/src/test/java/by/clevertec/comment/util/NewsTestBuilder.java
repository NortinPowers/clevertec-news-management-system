package by.clevertec.comment.util;

import static by.clevertec.comment.util.TestConstant.AUTHOR_ID;
import static by.clevertec.comment.util.TestConstant.AUTHOR_NAME;
import static by.clevertec.comment.util.TestConstant.NEWS_ID;
import static by.clevertec.comment.util.TestConstant.NEWS_TEXT;
import static by.clevertec.comment.util.TestConstant.NEWS_TIME;
import static by.clevertec.comment.util.TestConstant.NEWS_TITLE;

import by.clevertec.comment.domain.Author;
import by.clevertec.comment.domain.Comment;
import by.clevertec.comment.domain.News;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(setterPrefix = "with")
public class NewsTestBuilder {

    @Builder.Default
    private Long id = NEWS_ID;

    @Builder.Default
    private LocalDateTime time = NEWS_TIME;

    @Builder.Default
    private String title = NEWS_TITLE;

    @Builder.Default
    private String text = NEWS_TEXT;

    @Builder.Default
    private Long authorId = AUTHOR_ID;

    @Builder.Default
    private Author author = new Author(AUTHOR_NAME);

    @Builder.Default
    private List<Comment> comments = new ArrayList<>();

    public News buildNews() {
        News news = new News();
        news.setId(id);
        news.setTime(time);
        news.setTitle(title);
        news.setText(text);
        news.setComments(comments);
        news.setAuthor(author);
        return news;
    }
}
