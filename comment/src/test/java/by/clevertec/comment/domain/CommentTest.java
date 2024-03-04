package by.clevertec.comment.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


class CommentTest {

    @Test
    void preUpdateShouldSetDateToCreatedEntity() {
        Comment entity = new Comment();

        entity.preUpdate();

        assertNotNull(entity.getTime());
    }
}
