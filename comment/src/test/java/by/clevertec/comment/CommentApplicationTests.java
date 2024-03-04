package by.clevertec.comment;

import by.clevertec.comment.controller.CommentController;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@RequiredArgsConstructor
class CommentApplicationTests{
//class CommentApplicationTests extends AbstractTest{

    private final CommentController commentController;

    @Test
    void commentControllerMustBeNotNull_whenContextLoaded() {
        assertThat(commentController).isNotNull();
    }
}