package by.clevertec.comment.util;


import by.clevertec.comment.domain.Author;
import by.clevertec.comment.domain.Comment;
import by.clevertec.comment.domain.News;
import by.clevertec.request.NewsPathRequestDto;
import by.clevertec.request.NewsRequestDto;
import by.clevertec.response.NewsResponseDto;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static by.clevertec.comment.util.TestConstant.AUTHOR_ID;
import static by.clevertec.comment.util.TestConstant.AUTHOR_NAME;
import static by.clevertec.comment.util.TestConstant.NEWS_ID;
import static by.clevertec.comment.util.TestConstant.NEWS_TEXT;
import static by.clevertec.comment.util.TestConstant.NEWS_TIME;
import static by.clevertec.comment.util.TestConstant.NEWS_TITLE;

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
    private Long author_id = AUTHOR_ID;

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

    public NewsPathRequestDto buildNewsPathRequestDto() {
        NewsPathRequestDto news = new NewsPathRequestDto();
        news.setTitle(title);
        news.setText(text);
        return news;
    }

    public NewsRequestDto buildNewsRequestDto() {
        NewsRequestDto news = new NewsRequestDto();
        news.setTitle(title);
        news.setText(text);
        return news;
    }

    public NewsResponseDto buildNewsResponseDto() {
        NewsResponseDto news = new NewsResponseDto();
        news.setTime(time.toString());
        news.setTitle(title);
        news.setText(text);
        news.setAuthor(author.getName());
        return news;
    }
}
