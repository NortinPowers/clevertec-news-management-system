package by.clevertec.comment.domain;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class CommentTest {

    @Test
    void preUpdateShouldSetDateToCreatedEntity() {
        Comment entity = new Comment();

        entity.preUpdate();

        assertNotNull(entity.getTime());
    }
}
