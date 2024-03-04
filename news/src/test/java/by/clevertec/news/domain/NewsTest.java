package by.clevertec.news.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


class NewsTest {

    @Test
    void preUpdateShouldSetDateToCreatedEntity() {
        News entity = new News();

        entity.preUpdate();

        assertNotNull(entity.getTime());
    }
}
