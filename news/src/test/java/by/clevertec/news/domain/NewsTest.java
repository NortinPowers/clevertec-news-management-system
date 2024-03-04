package by.clevertec.news.domain;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;


class NewsTest {

    @Test
    void preUpdateShouldSetDateToCreatedEntity() {
        News entity = new News();

        entity.preUpdate();

        assertNotNull(entity.getTime());
    }
}
