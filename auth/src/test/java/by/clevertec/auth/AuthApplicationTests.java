package by.clevertec.auth;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import by.clevertec.auth.controller.AdminController;
import by.clevertec.auth.controller.AuthController;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@RequiredArgsConstructor
class AuthApplicationTests {

    private final AdminController adminController;
    private final AuthController authController;

    @Test
    void adminControllerMustBeNotNull_whenContextLoaded() {
        assertThat(adminController).isNotNull();
    }

    @Test
    void authControllerMustBeNotNull_whenContextLoaded() {
        assertThat(authController).isNotNull();
    }
}
