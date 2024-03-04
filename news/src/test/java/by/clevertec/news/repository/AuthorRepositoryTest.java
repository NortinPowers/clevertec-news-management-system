package by.clevertec.news.repository;

import by.clevertec.news.config.TestContainerConfig;
import by.clevertec.news.domain.Author;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.shaded.org.checkerframework.checker.units.qual.A;

import java.util.Optional;

import static by.clevertec.news.util.TestConstant.AUTHOR_NAME;
import static by.clevertec.news.util.TestConstant.NEWS_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@Transactional
@RequiredArgsConstructor
@Import(TestContainerConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(value = "classpath:sql/insert-before.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class AuthorRepositoryTest {

    private final AuthorRepository authorRepository;

    @Test
    void findByNameShouldReturnAuthor_whenAuthorExistInTable() {
        Author expected = new Author(AUTHOR_NAME);

        Optional<Author> actual = authorRepository.findByName(AUTHOR_NAME);

        assertTrue(actual.isPresent());
        assertThat(actual.get())
                .hasFieldOrPropertyWithValue(Author.Fields.name, expected.getName());
    }
}