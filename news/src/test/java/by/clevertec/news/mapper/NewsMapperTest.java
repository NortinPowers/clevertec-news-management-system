package by.clevertec.news.mapper;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import by.clevertec.news.domain.News;
import by.clevertec.news.util.NewsTestBuilder;
import by.clevertec.request.NewsPathRequestDto;
import by.clevertec.request.NewsRequestDto;
import by.clevertec.response.NewsResponseDto;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@RequiredArgsConstructor
//@Transactional
//@Import(TestContainerConfig.class)
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class NewsMapperTest {

    private final NewsMapper newsMapper;

    @Test
    void toDtoShouldReturnNewsResponseDto_whenNewsPassed() {
        News news = NewsTestBuilder.builder()
                .build()
                .buildNews();
        NewsResponseDto expected = NewsTestBuilder.builder()
                .build()
                .buildNewsResponseDto();

        NewsResponseDto actual = newsMapper.toDto(news);

        assertThat(actual)
                .hasFieldOrPropertyWithValue(News.Fields.time, expected.getTime())
                .hasFieldOrPropertyWithValue(News.Fields.title, expected.getTitle())
                .hasFieldOrPropertyWithValue(News.Fields.text, expected.getText());
    }

    @Test
    void newsRequestDtoToDomainShouldReturnNews_whenNewsRequestDtoPassed() {
        NewsRequestDto newsRequestDto = NewsTestBuilder.builder()
                .build()
                .buildNewsRequestDto();
        News expected = NewsTestBuilder.builder()
                .build()
                .buildNews();

        News actual = newsMapper.toDomain(newsRequestDto);

        assertThat(actual)
                .hasFieldOrPropertyWithValue(News.Fields.title, expected.getTitle())
                .hasFieldOrPropertyWithValue(News.Fields.text, expected.getText());
    }

    @Test
    void newsPathRequestDtoToDomainShouldReturnNews_whenNewsPathRequestDtoPassed() {
        NewsPathRequestDto newsPathRequestDto = NewsTestBuilder.builder()
                .build()
                .buildNewsPathRequestDto();
        News expected = NewsTestBuilder.builder()
                .build()
                .buildNews();

        News actual = newsMapper.toDomain(newsPathRequestDto);

        assertThat(actual)
                .hasFieldOrPropertyWithValue(News.Fields.title, expected.getTitle())
                .hasFieldOrPropertyWithValue(News.Fields.text, expected.getText());
    }

    @Test
    void mergeShouldReturnUpdatedNews_whenNewsForUpdatePassed() {
        String text = "new text";
        News news = NewsTestBuilder.builder()
                .build()
                .buildNews();
        News updated = NewsTestBuilder.builder()
                .withText(text)
                .build()
                .buildNews();
        News expected = NewsTestBuilder.builder()
                .withText(text)
                .build()
                .buildNews();

        News actual = newsMapper.merge(news, updated);

        assertThat(actual)
                .hasFieldOrPropertyWithValue(News.Fields.id, expected.getId())
                .hasFieldOrPropertyWithValue(News.Fields.time, expected.getTime())
                .hasFieldOrPropertyWithValue(News.Fields.title, expected.getTitle())
                .hasFieldOrPropertyWithValue(News.Fields.text, expected.getText());
    }
}
