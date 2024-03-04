package by.clevertec.news.repository;

import by.clevertec.aspect.RepositoryAspectLogger;
import by.clevertec.news.domain.Comment;
import by.clevertec.news.domain.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {

    /**
     * Получает список объектов {@link News} с учетом параметров страницы и размера страницы.
     *
     * @param pageable Объект Pageable.
     * @return Список объектов {@link Page <News>} для текущей страницы и размера страницы обернутый в Page.
     */
    @Override
    @RepositoryAspectLogger
    @EntityGraph(attributePaths = "author")
    Page<News> findAll(Pageable pageable);


    /**
     * Получает объект {@link News} по уникальному идентификатору (Long).
     *
     * @param id Уникальный идентификатор (Long) объекта {@link News}.
     * @return Объект {@link Optional <News>}, соответствующий указанному идентификатору.
     */
    @RepositoryAspectLogger
    @EntityGraph(attributePaths = "author")
    Optional<News> findById(Long id);

    /**
     * Удаляет объект {@link News} по уникальному идентификатору (Long).
     *
     * @param id Уникальный идентификатор (Long) объекта {@link News}, который требуется удалить.
     */
    @RepositoryAspectLogger
    void deleteById(Long id);

    /**
     * Метод для поиска новости по заданным критериям: названию, тексту или автору и сортировкой по дате в убывающем порядке.
     *
     * @param condition Условие поиска, которое может соответствовать части текста в названии, тексте или имени автора.
     * @param pageable  Интерфейс для представления информации о странице результатов запроса.
     * @return Страница с результатами поиска людей.
     */
    @RepositoryAspectLogger
    @Query("SELECT DISTINCT n FROM News n "
            + "JOIN FETCH Author a ON n.author = a "
            + "WHERE (LOWER(n.title) LIKE LOWER(CONCAT('%', :condition, '%')) "
            + "OR LOWER(n.text) LIKE LOWER(CONCAT('%', :condition, '%')) "
            + "OR LOWER(a.name) LIKE LOWER(CONCAT('%', :condition, '%'))) "
            + "ORDER BY n.time DESC")
    @EntityGraph(value = "news-with-author", type = EntityGraph.EntityGraphType.LOAD)
    Page<News> getNewsSearchResult(String condition, Pageable pageable);
}
