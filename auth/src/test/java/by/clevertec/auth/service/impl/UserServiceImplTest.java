package by.clevertec.auth.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import by.clevertec.auth.UserDto;
import by.clevertec.auth.domain.Role;
import by.clevertec.auth.domain.User;
import by.clevertec.auth.dto.UserRegistrationDto;
import by.clevertec.auth.mapper.UserMapper;
import by.clevertec.auth.repository.RoleRepository;
import by.clevertec.auth.repository.UserRepository;
import by.clevertec.auth.service.UserService;
import by.clevertec.auth.util.RoleTestBuilder;
import by.clevertec.auth.util.UserTestBuilder;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.datasource.lookup.DataSourceLookupFailureException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserMapper userMapper;

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private BCryptPasswordEncoder passwordEncoder;

    private final User user;
    private final Role role;

    {
//        user = new User();
//        user.setId(1L);
//        user.setUsername("User");
//        user.setPassword("Password");
//        role = new Role();
//        role.setId(1L);
//        role.setName("ROLE_USER");
//        user.setRole(role);
        role = RoleTestBuilder.builder()
                .build()
                .buildRole();
        user = UserTestBuilder.builder()
                .build()
                .buildUser();
    }

    @Test
    void getAllUsersShouldReturnUserDtosList() {
        List<User> users = List.of(user);
//        UserDto userDto = new UserDto();
//        userDto.setId(user.getId());
//        userDto.setUsername(user.getUsername());
//        userDto.setRole(user.getRole().getName());
        UserDto userDto = UserTestBuilder.builder()
                .build()
                .buildUserDto();
        List<UserDto> expected = List.of(userDto);

        when(userRepository.findAll())
                .thenReturn(users);
        when(userMapper.toDto(user))
                .thenReturn(userDto);

        List<UserDto> actual = userService.getAllUsers();

        assertEquals(expected, actual);
    }

    @Nested
    class TestGetUserByUsername {

        @Test
        void getUserByUsernameShouldReturnUser() {
            when(userRepository.findByUsername(user.getUsername()))
                    .thenReturn(Optional.of(user));

            User actual = userService.getUserByUsername(user.getUsername());

            assertEquals(user, actual);
        }

        @Test
        void getUserByUsernameShouldThrowUsernameNotFoundException_whenUserNotFound() {
            when(userRepository.findByUsername(any()))
                    .thenThrow(new UsernameNotFoundException("not matter"));

            assertThrows(UsernameNotFoundException.class, () -> userService.getUserByUsername(any()));
        }
    }

    @Nested
    class TestSaveUser {

        private final UserRegistrationDto userRegistrationDto = UserTestBuilder.builder()
                .build()
                .buildUserRegistrationDto();

//        {
//            userRegistrationDto = new UserRegistrationDto();
//            userRegistrationDto.setUsername(user.getUsername());
//            userRegistrationDto.setPassword(user.getPassword());
//            userRegistrationDto.setVerifyPassword(user.getPassword());
//        }

        @Test
        void saveShouldSaveUser() {
            user.setRole(null);
            String notEncryptedPassword = userRegistrationDto.getPassword();

            when(passwordEncoder.encode(user.getPassword()))
                    .thenReturn("encrypted password");
            when(userMapper.toDomain(userRegistrationDto))
                    .thenReturn(user);
            when(roleRepository.findByName(any()))
                    .thenReturn(Optional.of(role));
            when(userRepository.save(user))
                    .thenReturn(user);

            userService.save(userRegistrationDto);

            assertEquals(user.getRole(), role);
            assertNotEquals(notEncryptedPassword, userRegistrationDto.getPassword());
        }

        @Test
        void saveShouldThrowDataSourceLookupFailureException_whenRoleNotFound() {
            when(passwordEncoder.encode(user.getPassword()))
                    .thenReturn("encrypted password");
            when(userMapper.toDomain(userRegistrationDto))
                    .thenReturn(user);
            when(roleRepository.findByName(user.getRole()
                    .getName())).thenReturn(Optional.empty());

            assertThrows(DataSourceLookupFailureException.class, () -> userService.save(userRegistrationDto));

            verify(userRepository, never()).save(user);
        }
    }

    @Nested
    class TestIsUserExist {

        private Optional<User> optionalUser;

        @Test
        void isUserExistShouldReturnTrue_whenUserFound() {
            optionalUser = Optional.of(user);

            when(userRepository.findByUsername(any()))
                    .thenReturn(optionalUser);

            assertTrue(userService.isUserExist(any()));
        }

        @Test
        void isUserExistShouldReturnFalse_whenUserNotFound() {
            optionalUser = Optional.empty();

            when(userRepository.findByUsername(any()))
                    .thenReturn(optionalUser);

            assertFalse(userService.isUserExist(any()));
        }
    }

    @Nested
    class TestSetRoleAdmin {

//        private final Role roleAdmin;
//        private final String roleAdminName;
//
//        {
//            roleAdminName = "ROLE_ADMIN";
//            roleAdmin = new Role();
//            roleAdmin.setId(2L);
//            roleAdmin.setName(roleAdminName);
//        }

        @Test
        void setRoleAdminShouldSetRoleAdmin() {
            Role role = RoleTestBuilder.builder()
                    .withName("ROLE_ADMIN")
                    .build()
                    .buildRole();

            when(userRepository.findById(user.getId()))
                    .thenReturn(Optional.of(user));
            when(roleRepository.findByName("ROLE_ADMIN"))
                    .thenReturn(Optional.of(role));
            when(userRepository.save(user))
                    .thenReturn(user);

            userService.setRoleAdmin(user.getId());

            assertEquals(user.getRole().getName(), role.getName());
        }

        @Test
        void setRoleAdminShouldThrowUsernameNotFoundException_whenUserNotFound() {
            when(userRepository.findById(user.getId()))
                    .thenReturn(Optional.empty());

            assertThrows(EntityNotFoundException.class, () -> userService.setRoleAdmin(user.getId()));

            verifyNoInteractions(roleRepository);
            verify(userRepository, never()).save(user);
        }

        @Test
        void setRoleAdminShouldThrowDataSourceLookupFailureException_whenRoleNotFound() {
            when(userRepository.findById(user.getId()))
                    .thenReturn(Optional.of(user));
            when(roleRepository.findByName(any(String.class)))
                    .thenReturn(Optional.empty());

            assertThrows(DataSourceLookupFailureException.class, () -> userService.setRoleAdmin(user.getId()));

            verify(userRepository, never()).save(user);
        }
    }
}
