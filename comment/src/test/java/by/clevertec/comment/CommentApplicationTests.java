package by.clevertec.comment;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import by.clevertec.comment.controller.CommentController;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@RequiredArgsConstructor
class CommentApplicationTests {

    private final CommentController commentController;

    @Test
    void commentControllerMustBeNotNull_whenContextLoaded() {
        assertThat(commentController).isNotNull();
    }
}
