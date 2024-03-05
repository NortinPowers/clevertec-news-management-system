package by.clevertec.auth.repository;

import by.clevertec.aspect.RepositoryAspectLogger;
import by.clevertec.auth.domain.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @RepositoryAspectLogger
    Optional<User> findByUsername(String username);
}
