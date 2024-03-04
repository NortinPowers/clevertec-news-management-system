package by.clevertec.comment.domain;

import by.clevertec.comment.domain.News;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;


class NewsTest {

    @Test
    void preUpdateShouldSetDateToCreatedEntity() {
        News entity = new News();

        entity.preUpdate();

        LocalDateTime expectedUpdateDate = LocalDateTime.now();

        assertEquals(expectedUpdateDate, entity.getTime());
    }
}
