package by.clevertec.news.service.impl;

import by.clevertec.news.cache.Cache;
import by.clevertec.news.domain.News;
import by.clevertec.news.mapper.NewsMapper;
import by.clevertec.news.proxy.NewsCacheableAspect;
import by.clevertec.news.repository.NewsRepository;
import by.clevertec.news.util.NewsTestBuilder;
import by.clevertec.news.proxy.NewsCacheableAspect;
import static org.assertj.core.api.Assertions.assertThat;
import by.clevertec.response.NewsResponseDto;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static by.clevertec.news.util.TestConstant.NEWS_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
@RequiredArgsConstructor
public class NewsServiceImplCacheIntegrationTest  {

    private final NewsCacheableAspect cacheableAspect;
    private final NewsServiceImpl newsService;

    @MockBean
    private NewsRepository newsRepository;

    @MockBean
    private NewsMapper newsMapper;

    @MockBean
    private Cache<Long, Object> cache;

    @MockBean
    private ProceedingJoinPoint joinPoint;

    @Test
    void getShouldReturnNewsResponseDtoFromAop_whenNewsResponseDtoWithCurrentIdIsNotInCacheButIsInRepository() throws Throwable {
        NewsResponseDto expected = NewsTestBuilder.builder()
                .build()
                .buildNewsResponseDto();

        lenient()
                .when(joinPoint.getArgs())
                .thenReturn(new Object[]{NEWS_ID});
        lenient()
                .when(cache.get(NEWS_ID))
                .thenReturn(null);
        when(joinPoint.proceed())
                .thenReturn(expected);

        NewsResponseDto actual = (NewsResponseDto) cacheableAspect.cacheableGet(joinPoint);

        assertThat(actual)
                .hasFieldOrPropertyWithValue(News.Fields.time, expected.getTime())
                .hasFieldOrPropertyWithValue(News.Fields.title, expected.getTitle())
                .hasFieldOrPropertyWithValue(News.Fields.text, expected.getText())
                .hasFieldOrPropertyWithValue("author", expected.getAuthor());
    }

    @Test
    void getShouldReturnNewsResponseDtoFromRepository_whenNewsWithCurrentUuidExist() {
        NewsResponseDto expected = NewsTestBuilder.builder()
                .build()
                .buildNewsResponseDto();
        News news = NewsTestBuilder.builder()
                .build()
                .buildNews();

        when(newsRepository.findById(NEWS_ID))
                .thenReturn(Optional.of(news));
        when(newsMapper.toDto(news))
                .thenReturn(expected);

        NewsResponseDto actual = newsService.getById(NEWS_ID);

        assertThat(actual)
                .hasFieldOrPropertyWithValue(News.Fields.time, expected.getTime())
                .hasFieldOrPropertyWithValue(News.Fields.title, expected.getTitle())
                .hasFieldOrPropertyWithValue(News.Fields.text, expected.getText())
                .hasFieldOrPropertyWithValue("author", expected.getAuthor());
    }
}
