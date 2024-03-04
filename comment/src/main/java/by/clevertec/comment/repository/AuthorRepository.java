package by.clevertec.comment.repository;

import by.clevertec.aspect.RepositoryAspectLogger;
import by.clevertec.comment.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    @RepositoryAspectLogger
    Optional<Author> findByName(String name);
}
