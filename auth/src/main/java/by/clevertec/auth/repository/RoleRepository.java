package by.clevertec.auth.repository;


import by.clevertec.aspect.RepositoryAspectLogger;
import by.clevertec.auth.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    @RepositoryAspectLogger
    Optional<Role> findByName(String name);
}
