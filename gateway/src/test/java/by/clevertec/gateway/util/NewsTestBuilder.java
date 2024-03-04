package by.clevertec.gateway.util;


import static by.clevertec.gateway.util.TestConstant.AUTHOR_ID;
import static by.clevertec.gateway.util.TestConstant.AUTHOR_NAME;
import static by.clevertec.gateway.util.TestConstant.NEWS_ID;
import static by.clevertec.gateway.util.TestConstant.NEWS_TEXT;
import static by.clevertec.gateway.util.TestConstant.NEWS_TIME;
import static by.clevertec.gateway.util.TestConstant.NEWS_TITLE;

import by.clevertec.request.NewsAndNamePathRequestDto;
import by.clevertec.request.NewsAndNameRequestDto;
import by.clevertec.request.NewsPathRequestDto;
import by.clevertec.request.NewsRequestDto;
import by.clevertec.response.NewsResponseDto;
import java.time.LocalDateTime;
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
        news.setAuthor(AUTHOR_NAME);
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
