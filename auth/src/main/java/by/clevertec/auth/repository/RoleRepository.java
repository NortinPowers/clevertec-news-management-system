package by.clevertec.auth.repository;

import by.clevertec.aspect.RepositoryAspectLogger;
import by.clevertec.auth.domain.Role;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * Ищет роль по её имени.
     *
     * @param name Имя роли для поиска.
     * @return Объект {@link Optional<Role>}, содержащий роль с указанным именем или пустой, если роль не найдена.
     */
    @RepositoryAspectLogger
    Optional<Role> findByName(String name);
}
