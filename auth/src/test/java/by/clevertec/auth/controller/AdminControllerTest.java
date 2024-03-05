package by.clevertec.auth.controller;

import static by.clevertec.auth.utils.Constants.ENTITY_NOT_FOUND_EXCEPTION_MESSAGE;
import static by.clevertec.utils.ResponseUtils.CHANGE_ROLE_MESSAGE;
import static by.clevertec.utils.ResponseUtils.DATA_SOURCE_LOOKUP_FAILURE_EXCEPTION_MESSAGE;
import static by.clevertec.utils.ResponseUtils.getExceptionResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import by.clevertec.auth.UserDto;
import by.clevertec.auth.config.TestContainerConfig;
import by.clevertec.auth.service.AdminService;
import by.clevertec.message.ExceptionResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.datasource.lookup.DataSourceLookupFailureException;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@AutoConfigureMockMvc
@ActiveProfiles("test")
@SpringBootTest(classes = TestContainerConfig.class)
@Sql(value = "classpath:sql/user/user-repository-before.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "classpath:sql/user/user-repository-after.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminService adminService;

    private final ExceptionResponse exceptionResponse;

    @Autowired
    private ObjectMapper mapper;
    private String url;

    {
        exceptionResponse = new ExceptionResponse(
                FORBIDDEN,
                "Access Denied",
                "AccessDeniedException"
        );
    }

    @Nested
    class TestSetAdmin {

        private final Long id;

        {
            url = "/admin/set/{id}";
            id = 1L;
        }

        @Test
        @WithAnonymousUser
        void setAdminShouldReturnMessageAboutTokenNeeded_whenUserIsAnonymous() throws Exception {
            mockMvc.perform(post(url, id))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("error").value("To get access you need token"));
        }

        @Test
        @WithMockUser(username = "user", roles = "SUBSCRIBER")
        void setAdminShouldReturnAccessDeniedMessage_whenUserIsSubscriber() throws Exception {
            mockMvc.perform(post(url, id))
                    .andExpect(status().isForbidden())
                    .andExpectAll(
                            jsonPath("$.timestamp").isNotEmpty(),
                            jsonPath("$.status").value(exceptionResponse.getStatus()),
                            jsonPath("$.message").value(exceptionResponse.getMessage()),
                            jsonPath("$.type").value(exceptionResponse.getType()));
        }

        @Test
        @WithMockUser(username = "user", roles = "JOURNALIST")
        void setAdminShouldReturnAccessDeniedMessage_whenUserIsJournalist() throws Exception {
            mockMvc.perform(post(url, id))
                    .andExpect(status().isForbidden())
                    .andExpectAll(
                            jsonPath("$.timestamp").isNotEmpty(),
                            jsonPath("$.status").value(exceptionResponse.getStatus()),
                            jsonPath("$.message").value(exceptionResponse.getMessage()),
                            jsonPath("$.type").value(exceptionResponse.getType()));
        }

        @Test
        @WithMockUser(username = "admin", roles = "ADMIN")
        void setAdminShouldReturnSuccessResponse_whenUserIsAdmin() throws Exception {
            doNothing()
                    .when(adminService).setAdmin(id);

            mockMvc.perform(post(url, id))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("message").value(String.format(CHANGE_ROLE_MESSAGE, "user")));
        }

        @Test
        @WithMockUser(username = "admin", roles = "ADMIN")
        void setAdminShouldReturnExceptionResponse_whenDbError() throws Exception {
            DataSourceLookupFailureException exception = new DataSourceLookupFailureException("it does not matter");
            ExceptionResponse response = getExceptionResponse(
                    INTERNAL_SERVER_ERROR,
                    DATA_SOURCE_LOOKUP_FAILURE_EXCEPTION_MESSAGE,
                    exception
            );

            doThrow(exception)
                    .when(adminService).setAdmin(any());

            mockMvc.perform(post(url, id))
                    .andExpect(status().isInternalServerError())
                    .andExpectAll(
                            jsonPath("$.timestamp").isNotEmpty(),
                            jsonPath("$.status").value(response.getStatus()),
                            jsonPath("$.message").value(response.getMessage()),
                            jsonPath("$.type").value(response.getType()));
        }

        @Test
        @WithMockUser(username = "admin", roles = "ADMIN")
        void setAdminShouldReturnExceptionResponse_whenUserNotFound() throws Exception {
            EntityNotFoundException exception = new EntityNotFoundException(ENTITY_NOT_FOUND_EXCEPTION_MESSAGE);
            ExceptionResponse response = getExceptionResponse(
                    NOT_FOUND,
                    ENTITY_NOT_FOUND_EXCEPTION_MESSAGE,
                    exception
            );

            doThrow(exception)
                    .when(adminService).setAdmin(any());

            mockMvc.perform(post(url, id))
                    .andExpect(status().isNotFound())
                    .andExpectAll(
                            jsonPath("$.timestamp").isNotEmpty(),
                            jsonPath("$.status").value(response.getStatus()),
                            jsonPath("$.message").value(response.getMessage()),
                            jsonPath("$.type").value(response.getType()));
        }
    }

    @Nested
    class TestGetAllUser {

        {
            url = "/admin/users";
        }

        @Test
        @WithAnonymousUser
        void getAllUserShouldReturnMessageAboutTokenNeeded_whenUserIsAnonymous() throws Exception {
            mockMvc.perform(get(url))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("error").value("To get access you need token"));

        }

        @Test
        @WithMockUser(username = "user", roles = "SUBSCRIBER")
        void getAllUserShouldReturnAccessDeniedMessage_whenUserIsSubscriber() throws Exception {

            mockMvc.perform(get(url))
                    .andExpect(status().isForbidden())
                    .andExpectAll(
                            jsonPath("$.timestamp").isNotEmpty(),
                            jsonPath("$.status").value(exceptionResponse.getStatus()),
                            jsonPath("$.message").value(exceptionResponse.getMessage()),
                            jsonPath("$.type").value(exceptionResponse.getType()));
        }

        @Test
        @WithMockUser(username = "user", roles = "JOURNALIST")
        void getAllUserShouldReturnAccessDeniedMessage_whenUserIsJournalist() throws Exception {

            mockMvc.perform(get(url))
                    .andExpect(status().isForbidden())
                    .andExpectAll(
                            jsonPath("$.timestamp").isNotEmpty(),
                            jsonPath("$.status").value(exceptionResponse.getStatus()),
                            jsonPath("$.message").value(exceptionResponse.getMessage()),
                            jsonPath("$.type").value(exceptionResponse.getType()));
        }

        @Test
        @WithMockUser(username = "admin", roles = "ADMIN")
        void getAllUserShouldReturnUsersList_whenUserIsAdmin() throws Exception {
            UserDto firstUser = new UserDto();
            firstUser.setId(1L);
            firstUser.setUsername("admin");
            firstUser.setRole("ROLE_ADMIN");
            UserDto secondUser = new UserDto();
            secondUser.setId(2L);
            secondUser.setUsername("user");
            secondUser.setRole("ROLE_SUBSCRIBER");
            List<UserDto> users = List.of(firstUser, secondUser);

            when(adminService.getAllUsers())
                    .thenReturn(users);

            mockMvc.perform(get(url))
                    .andExpect(status().isOk())
                    .andExpect(content().json(mapper.writeValueAsString(users)));
        }
    }
}
