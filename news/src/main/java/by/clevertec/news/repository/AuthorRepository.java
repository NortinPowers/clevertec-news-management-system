package by.clevertec.news.repository;

import by.clevertec.aspect.RepositoryAspectLogger;
import by.clevertec.news.domain.Author;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    @RepositoryAspectLogger
    Optional<Author> findByName(String name);
}
