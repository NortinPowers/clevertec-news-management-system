package by.clevertec.news.util;


import static by.clevertec.news.util.TestConstant.AUTHOR_ID;
import static by.clevertec.news.util.TestConstant.AUTHOR_NAME;
import static by.clevertec.news.util.TestConstant.NEWS_ID;
import static by.clevertec.news.util.TestConstant.NEWS_TEXT;
import static by.clevertec.news.util.TestConstant.NEWS_TIME;
import static by.clevertec.news.util.TestConstant.NEWS_TITLE;

import by.clevertec.news.domain.Author;
import by.clevertec.news.domain.Comment;
import by.clevertec.news.domain.News;
import by.clevertec.request.NewsAndNamePathRequestDto;
import by.clevertec.request.NewsAndNameRequestDto;
import by.clevertec.request.NewsPathRequestDto;
import by.clevertec.request.NewsRequestDto;
import by.clevertec.response.NewsResponseDto;
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

    public NewsAndNamePathRequestDto buildNewsAndNamePathRequestDto() {
        NewsAndNamePathRequestDto requestDto = new NewsAndNamePathRequestDto();
        requestDto.setName(AUTHOR_NAME);
        NewsPathRequestDto news = new NewsPathRequestDto();
        news.setTitle("new title");
        news.setText(text);
        requestDto.setRequestDto(news);
        return requestDto;
    }

    public NewsAndNameRequestDto buildNewsAndNameRequestDto() {
        NewsAndNameRequestDto requestDto = new NewsAndNameRequestDto();
        requestDto.setName(AUTHOR_NAME);
        NewsRequestDto news = new NewsRequestDto();
        news.setTitle("new title");
        news.setText("new text");
        requestDto.setRequestDto(news);
        return requestDto;
    }
}
