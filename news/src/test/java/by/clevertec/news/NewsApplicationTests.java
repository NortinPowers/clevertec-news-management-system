package by.clevertec.news;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import by.clevertec.news.controller.NewsController;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

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
