package by.clevertec.auth.repository;

import by.clevertec.auth.config.TestContainerConfig;
import by.clevertec.auth.domain.Role;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test")
@RequiredArgsConstructor
@Import(TestContainerConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
@Sql(value = "classpath:sql/role/role-repository-before.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
//@Sql(value = "classpath:sql/role/role-repository-after.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    private String testRole;

    {
        testRole = "SUBSCRIBER";
    }

    @Test
    void test_findByName_isPresent() {
        Optional<Role> optionalRole = roleRepository.findByName(testRole);

        assertTrue(optionalRole.isPresent());
        assertEquals(testRole, optionalRole.get().getName());
    }

    @Test
    void test_findByName_isNotPresent() {
        testRole = "NotExistRole";

        Optional<Role> optionalRole = roleRepository.findByName(testRole);

        assertFalse(optionalRole.isPresent());
    }
}
