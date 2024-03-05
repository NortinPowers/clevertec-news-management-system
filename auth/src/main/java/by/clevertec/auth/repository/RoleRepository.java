package by.clevertec.auth.repository;


import by.clevertec.aspect.RepositoryAspectLogger;
import by.clevertec.auth.domain.Role;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

    @RepositoryAspectLogger
    Optional<Role> findByName(String name);
}
