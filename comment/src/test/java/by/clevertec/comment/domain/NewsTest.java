package by.clevertec.comment.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;


class NewsTest {

    @Test
    void preUpdateShouldSetDateToCreatedEntity() {
        News entity = new News();

        entity.preUpdate();

        LocalDateTime expectedUpdateDate = LocalDateTime.now();

        assertEquals(expectedUpdateDate, entity.getTime());
    }
}
