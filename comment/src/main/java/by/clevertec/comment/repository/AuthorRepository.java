package by.clevertec.comment.repository;

import by.clevertec.aspect.RepositoryAspectLogger;
import by.clevertec.comment.domain.Author;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    /**
     * Получает объект {@link Author} по уникальному имени (String).
     *
     * @param name Уникальное имя (String) объекта {@link Author}.
     * @return Объект {@link Optional <Author>}, соответствующий указанному имени.
     */
    @RepositoryAspectLogger
    Optional<Author> findByName(String name);
}
