package by.clevertec.auth.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import by.clevertec.auth.UserDto;
import by.clevertec.auth.service.AdminService;
import by.clevertec.auth.service.UserService;
import by.clevertec.auth.util.UserTestBuilder;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.datasource.lookup.DataSourceLookupFailureException;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@RequiredArgsConstructor
class AdminServiceImplTest {

    private final AdminService adminService;

    @MockBean
    private UserService userService;

    @Nested
    class TestSetAdmin {

        @Test
        void setAdminShouldChangeRole() {
            doNothing()
                    .when(userService).setRoleAdmin(any());

            adminService.setAdmin(any());
        }

        @Test
        void setAdminShouldThrowDataSourceLookupFailureException_whenDbException() {
            DataSourceLookupFailureException exception = new DataSourceLookupFailureException("not matter");

            doThrow(exception)
                    .when(userService).setRoleAdmin(any());

            assertThrows(DataSourceLookupFailureException.class, () -> userService.setRoleAdmin(any()));
        }
    }

    @Nested
    class TestSetJournalist {

        @Test
        void setJournalistShouldChangeRole() {
            doNothing()
                    .when(userService).setRoleJournalist(any());

            adminService.setAdmin(any());
        }

        @Test
        void setJournalistShouldThrowDataSourceLookupFailureException_whenDbException() {
            DataSourceLookupFailureException exception = new DataSourceLookupFailureException("not matter");

            doThrow(exception)
                    .when(userService).setRoleJournalist(any());

            assertThrows(DataSourceLookupFailureException.class, () -> userService.setRoleJournalist(any()));
        }
    }

    @Nested
    class TestGetAllUsers {

        private final List<UserDto> users = new ArrayList<>();

        @Test
        void getAllUsersShouldReturnEmptyList_whenNoUsersExist() {
            when(userService.getAllUsers())
                    .thenReturn(users);

            List<UserDto> actual = adminService.getAllUsers();

            assertEquals(users, actual);
        }

        @Test
        void getAllUsersShouldReturnUserDtosList_whenUsersExist() {
            UserDto userDto = UserTestBuilder.builder()
                    .build()
                    .buildUserDto();
            users.add(userDto);

            when(userService.getAllUsers())
                    .thenReturn(users);

            List<UserDto> actual = adminService.getAllUsers();

            assertEquals(users, actual);
        }
    }
}
