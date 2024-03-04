package by.clevertec.news;

import by.clevertec.news.config.TestContainerConfig;
import by.clevertec.news.controller.NewsController;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@RequiredArgsConstructor
class NewsApplicationTests {
//class NewsApplicationTests extends AbstractTest{

    private final NewsController newsController;

    @Test
    void newsControllerMustBeNotNull_whenContextLoaded() {
        assertThat(newsController).isNotNull();
    }
}
