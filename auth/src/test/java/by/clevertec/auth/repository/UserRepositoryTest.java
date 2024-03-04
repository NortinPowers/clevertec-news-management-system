package by.clevertec.auth.repository;

import by.clevertec.auth.config.TestContainerConfig;
import by.clevertec.auth.domain.User;
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
@Import(TestContainerConfig.class)
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(value = "classpath:sql/user/user-repository-before.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
//@Sql(value = "classpath:sql/user/user-repository-after.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private String username;

    {
        username = "admin";
    }

    @Test
    void test_findByUsername_isPresent() {
        Optional<User> optionalUser = userRepository.findByUsername(username);

        assertTrue(optionalUser.isPresent());
        assertEquals(username, optionalUser.get().getUsername());
    }

    @Test
    void test_findByUsername_isNotPresent() {
        username = "nonExistUser";
        Optional<User> optionalUser = userRepository.findByUsername(username);

        assertFalse(optionalUser.isPresent());
    }
}
