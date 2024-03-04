package by.clevertec.comment.repository;

import by.clevertec.aspect.RepositoryAspectLogger;
import by.clevertec.comment.domain.Comment;
import by.clevertec.comment.domain.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByNewsId(Long newsId);

    /**
     * Получает список объектов {@link Comment} с учетом параметров страницы и размера страницы.
     *
     * @param pageable Объект Pageable.
     * @return Список объектов {@link Page <Comment>} для текущей страницы и размера страницы обернутый в Page.
     */
    @Override
    @RepositoryAspectLogger
    @EntityGraph(attributePaths = {"news", "author"})
    Page<Comment> findAll(Pageable pageable);


    /**
     * Получает объект {@link Comment} по уникальному идентификатору (Long).
     *
     * @param id Уникальный идентификатор (Long) объекта {@link Comment}.
     * @return Объект {@link Optional <Comment>}, соответствующий указанному идентификатору.
     */
    @RepositoryAspectLogger
    @EntityGraph(attributePaths = {"news", "author"})
    Optional<Comment> findById(Long id);

    /**
     * Удаляет объект {@link Comment} по уникальному идентификатору (Long).
     *
     * @param id Уникальный идентификатор (Long) объекта {@link Comment}, который требуется удалить.
     */
    @RepositoryAspectLogger
    void deleteById(Long id);

    @RepositoryAspectLogger
    Page<Comment> findByNewsId(Long newsId, Pageable pageable);

    /**
     * Метод для поиска комментариев по заданным критериям: имени комментатора, тексту, заголовку новости или автору и сортировкой по дате в убывающем порядке.
     *
     * @param condition Условие поиска, которое может соответствовать части текста в имени комментатора, тексте, заголовке новости или имени автора.
     * @param pageable  Интерфейс для представления информации о странице результатов запроса.
     * @return Страница с результатами поиска людей.
     */
    @RepositoryAspectLogger
    @Query("SELECT DISTINCT c FROM Comment c "
            + "JOIN FETCH Author a ON c.author = a "
            + "WHERE (LOWER(c.username) LIKE LOWER(CONCAT('%', :condition, '%')) "
            + "OR LOWER(c.text) LIKE LOWER(CONCAT('%', :condition, '%')) "
            + "OR LOWER(a.name) LIKE LOWER(CONCAT('%', :condition, '%'))) "
            + "ORDER BY c.time DESC")
    @EntityGraph(value = "comment-with-news-id-and-author", type = EntityGraph.EntityGraphType.LOAD)
    Page<Comment> getCommentsSearchResult(String condition, Pageable pageable);
}
