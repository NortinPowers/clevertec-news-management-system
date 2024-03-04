package by.clevertec.news.repository;

import by.clevertec.aspect.RepositoryAspectLogger;
import by.clevertec.news.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    @RepositoryAspectLogger
    Optional<Author> findByName(String name);
}
