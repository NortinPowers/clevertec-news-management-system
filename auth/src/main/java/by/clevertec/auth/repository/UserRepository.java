package by.clevertec.auth.repository;

import by.clevertec.aspect.RepositoryAspectLogger;
import by.clevertec.auth.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @RepositoryAspectLogger
    Optional<User> findByUsername(String username);
}
