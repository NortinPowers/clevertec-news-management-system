package by.clevertec.gateway.util;

import static by.clevertec.gateway.util.TestConstant.AUTHOR_ID;
import static by.clevertec.gateway.util.TestConstant.AUTHOR_NAME;
import static by.clevertec.gateway.util.TestConstant.NEWS_ID;
import static by.clevertec.gateway.util.TestConstant.NEWS_TEXT;
import static by.clevertec.gateway.util.TestConstant.NEWS_TIME;
import static by.clevertec.gateway.util.TestConstant.NEWS_TITLE;

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
    private Long authorId = AUTHOR_ID;

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
}
