package by.clevertec.auth.service;

import by.clevertec.auth.domain.User;
import by.clevertec.auth.util.UserTestBuilder;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
@RequiredArgsConstructor
class CustomUserDetailsServiceTest {

    @Autowired
    private final CustomUserDetailsService customUserDetailsService;

    @MockBean
    private UserService userService;

//    private final User user;
//
//    {
//        Long id = 1L;
//        String username = "user";
//        String password = "password";
//        String roleName = "ROLE_USER";
//        Role role = new Role();
//        role.setId(id);
//        role.setName(roleName);
//        user = new User();
//        user.setId(id);
//        user.setUserName(username);
//        user.setPassword(password);
//        user.setRole(role);
//    }

    @Test
    void test_loadUserByUsername_isPresent() {
        User user = UserTestBuilder.builder()
                .build()
                .buildUser();

        when(userService.getUserByUsername(user.getUsername()))
                .thenReturn(user);

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(user.getUsername());

        assertEquals(userDetails.getUsername(), user.getUsername());

    }

    @Test
    void test_loadUserByUsername_isNotPresent() {
        User user = UserTestBuilder.builder()
                .build()
                .buildUser();

        when(userService.getUserByUsername(user.getUsername()))
                .thenThrow(new UsernameNotFoundException("User not found"));

        assertThrows(UsernameNotFoundException.class, () -> customUserDetailsService.loadUserByUsername(user.getUsername()));
    }
}
