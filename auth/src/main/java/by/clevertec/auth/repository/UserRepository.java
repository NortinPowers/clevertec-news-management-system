package by.clevertec.auth.repository;

import by.clevertec.aspect.RepositoryAspectLogger;
import by.clevertec.auth.domain.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Ищет пользователя по его имени пользователя.
     *
     * @param username Имя пользователя для поиска.
     * @return Объект {@link Optional<User>}, содержащий пользователя с указанным именем или пустой, если пользователь не найден.
     */
    @RepositoryAspectLogger
    Optional<User> findByUsername(String username);
}
