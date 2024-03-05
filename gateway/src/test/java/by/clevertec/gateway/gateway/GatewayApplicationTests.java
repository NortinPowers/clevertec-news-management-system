package by.clevertec.gateway.gateway;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import by.clevertec.gateway.controller.AdminController;
import by.clevertec.gateway.controller.AuthController;
import by.clevertec.gateway.controller.CommentController;
import by.clevertec.gateway.controller.NewsController;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@RequiredArgsConstructor
class GatewayApplicationTests {

    private final AdminController adminController;
    private final AuthController authController;
    private final CommentController commentController;
    private final NewsController newsController;

    @Test
    void adminControllerMustBeNotNull_whenContextLoaded() {
        assertThat(adminController).isNotNull();
    }

    @Test
    void authControllerMustBeNotNull_whenContextLoaded() {
        assertThat(authController).isNotNull();
    }

    @Test
    void commentControllerMustBeNotNull_whenContextLoaded() {
        assertThat(commentController).isNotNull();
    }

    @Test
    void newsControllerMustBeNotNull_whenContextLoaded() {
        assertThat(newsController).isNotNull();
    }
}
