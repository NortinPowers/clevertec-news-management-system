package by.clevertec.news.repository;

import by.clevertec.news.config.TestContainerConfig;
import by.clevertec.news.domain.News;
import by.clevertec.news.util.NewsTestBuilder;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static by.clevertec.news.util.TestConstant.NEWS_TITLE;
import static by.clevertec.news.util.TestConstant.PAGE_NUMBER;
import static by.clevertec.news.util.TestConstant.PAGE_SIZE;
import static by.clevertec.news.util.TestConstant.NEWS_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test")
@Transactional
@RequiredArgsConstructor
@Import(TestContainerConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)

//TODO @Transactional не работает?

@Sql(value = "classpath:sql/insert-before.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)

@Sql(value = "classpath:sql/reset-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class NewsRepositoryTest {

    private final NewsRepository newsRepository;

    @Test
    void findAllShouldReturnPageWithNews_whenNewsTableIsNotEmpty() {
        News expected = NewsTestBuilder.builder()
                .build()
                .buildNews();
        PageRequest pageRequest = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);

        Page<News> actual = newsRepository.findAll(pageRequest);

        assertThat(actual.getContent().get(0))
                .hasFieldOrPropertyWithValue(News.Fields.id, expected.getId())
                .hasFieldOrPropertyWithValue(News.Fields.time, expected.getTime())
                .hasFieldOrPropertyWithValue(News.Fields.title, expected.getTitle())
                .hasFieldOrPropertyWithValue(News.Fields.text, expected.getText());
//                .hasFieldOrPropertyWithValue(News.Fields.author, expected.getAuthor());
        assertEquals(expected.getAuthor().getName(), actual.getContent().get(0).getAuthor().getName());
    }

    @Test
    void findByIdShouldReturnNews_whenNewsExistInTable() {
        News expected = NewsTestBuilder.builder()
                .build()
                .buildNews();

        Optional<News> actual = newsRepository.findById(NEWS_ID);

        assertTrue(actual.isPresent());
        assertThat(actual.get())
                .hasFieldOrPropertyWithValue(News.Fields.id, expected.getId())
                .hasFieldOrPropertyWithValue(News.Fields.time, expected.getTime())
                .hasFieldOrPropertyWithValue(News.Fields.title, expected.getTitle())
                .hasFieldOrPropertyWithValue(News.Fields.text, expected.getText());
//                .hasFieldOrPropertyWithValue(News.Fields.author, expected.getAuthor());
        assertEquals(expected.getAuthor().getName(), actual.get().getAuthor().getName());
    }

    @Test
    //TODO
//    @Sql(value = {"classpath:sql/news/create-news-without-owner.sql", "classpath:sql/delete-owners.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void deleteByIdShouldDeleteNews_whenNewsExistInTable() {
        Optional<News> before = newsRepository.findById(NEWS_ID);
        assertTrue(before.isPresent());

        newsRepository.deleteById(NEWS_ID);

        Optional<News> after = newsRepository.findById(NEWS_ID);
        assertFalse(after.isPresent());
    }

    @Test
    void getNewsSearchResultShouldReturnPageOfNews_whenNewsWithSearchConditionExistInTable() {
        News expected = NewsTestBuilder.builder()
                .build()
                .buildNews();
        PageRequest pageRequest = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);

        Page<News> actual = newsRepository.getNewsSearchResult(NEWS_TITLE, pageRequest);

        assertThat(actual.getContent().get(0))
                .hasFieldOrPropertyWithValue(News.Fields.id, expected.getId())
                .hasFieldOrPropertyWithValue(News.Fields.time, expected.getTime())
                .hasFieldOrPropertyWithValue(News.Fields.title, expected.getTitle())
                .hasFieldOrPropertyWithValue(News.Fields.text, expected.getText());
//                .hasFieldOrPropertyWithValue(News.Fields.author, expected.getAuthor());
        assertEquals(expected.getAuthor().getName(), actual.getContent().get(0).getAuthor().getName());
    }

    @Test
    void getNewsSearchResultShouldReturnPageWithEmptyOptional_whenNewsWithSearchConditionNotExistInTable() {
        PageRequest pageRequest = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);

        Page<News> actual = newsRepository.getNewsSearchResult("some", pageRequest);

        Optional<News> newsOptional = actual.getContent().stream()
                .findFirst();

        assertThat(newsOptional).isEmpty();
    }

    @Test
    public void saveShouldNotThrowException_whenCalled() {
        News news = NewsTestBuilder.builder()
                .build()
                .buildNews();

        assertDoesNotThrow(() -> newsRepository.save(news));
    }
}
