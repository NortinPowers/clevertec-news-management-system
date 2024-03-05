package by.clevertec.auth.controller;


import static by.clevertec.auth.utils.ResponseUtils.HTTP_MESSAGE_NOT_READABLE_EXCEPTION_MESSAGE;
import static by.clevertec.utils.ResponseUtils.BAD_CREDENTIALS_EXCEPTION_MESSAGE;
import static by.clevertec.utils.ResponseUtils.CREATION_MESSAGE;
import static by.clevertec.utils.ResponseUtils.DATA_SOURCE_LOOKUP_FAILURE_EXCEPTION_MESSAGE;
import static by.clevertec.utils.ResponseUtils.METHOD_ARGUMENT_NOT_VALID_EXCEPTION_MESSAGE;
import static by.clevertec.utils.ResponseUtils.getExceptionResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import by.clevertec.auth.JwtRequest;
import by.clevertec.auth.JwtResponse;
import by.clevertec.auth.dto.UserRegistrationDto;
import by.clevertec.auth.service.AuthService;
import by.clevertec.auth.service.UserService;
import by.clevertec.message.ErrorValidationResponse;
import by.clevertec.message.ExceptionResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.jdbc.datasource.lookup.DataSourceLookupFailureException;
import org.springframework.mock.http.MockHttpInputMessage;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor
class AuthControllerTest {

    //    @Autowired
    private final MockMvc mockMvc;

    private final ObjectMapper mapper;

    @MockBean
    private AuthService authService;

    @MockBean
    private UserService userService;

    private String url;
    private ErrorValidationResponse errorValidationResponse;
    private List<String> errors;
    private ExceptionResponse response;
//
//    {
//        mapper = getMapperWithTimeModule();
//    }

    @Nested
    class TestCreateToken {

        private final JwtRequest jwtRequest;

        {
            url = "/auth";
            jwtRequest = new JwtRequest();
            jwtRequest.setUsername("user");
            jwtRequest.setPassword("password");
        }

        @Test
        void createAuthTokenShouldReturnJwtToken_whenCalled() throws Exception {
//        void test_createAuthToken_success() throws Exception {
            String token = "header.payload.signature";
            JwtResponse jwtResponse = new JwtResponse(token);

            when(authService.getToken(any()))
                    .thenReturn(token);

            mockMvc.perform(post(url)
                            .contentType(APPLICATION_JSON)
                            .content(mapper.writeValueAsString(jwtRequest)))
                    .andExpect(status().isOk())
                    .andExpect(content().json(mapper.writeValueAsString(jwtResponse)));
        }

        @Test
        void createAuthTokenShouldReturnExceptionResponse_whenEmptyUserData() throws Exception {
//        void test_createAuthToken_emptyBody() throws Exception {
            errors = List.of("Enter password", "Enter username");
            errorValidationResponse = new ErrorValidationResponse(
                    BAD_REQUEST,
                    errors,
                    METHOD_ARGUMENT_NOT_VALID_EXCEPTION_MESSAGE
            );
            jwtRequest.setUsername("");
            jwtRequest.setPassword("");

            mockMvc.perform(post(url)
                            .contentType(APPLICATION_JSON)
                            .content(mapper.writeValueAsString(jwtRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpectAll(
                            jsonPath("$.timestamp").isNotEmpty(),
                            jsonPath("$.status").value(errorValidationResponse.getStatus()),
                            jsonPath("$.message").value(errorValidationResponse.getMessage()),
                            jsonPath("$.errors.size()").value(errorValidationResponse.getErrors().size()));
        }

        @Test
        void createAuthTokenShouldReturnExceptionResponse_whenSemiEmptyBody() throws Exception {
//        void test_createAuthToken_semiEmptyBody() throws Exception {
            errors = List.of("Enter password");
            jwtRequest.setPassword("");
            errorValidationResponse = new ErrorValidationResponse(
                    BAD_REQUEST,
                    errors,
                    METHOD_ARGUMENT_NOT_VALID_EXCEPTION_MESSAGE
            );

            mockMvc.perform(post(url)
                            .contentType(APPLICATION_JSON)
                            .content(mapper.writeValueAsString(jwtRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpectAll(
                            jsonPath("$.timestamp").isNotEmpty(),
                            jsonPath("$.status").value(errorValidationResponse.getStatus()),
                            jsonPath("$.message").value(errorValidationResponse.getMessage()),
                            jsonPath("$.errors.[0]").value(errorValidationResponse.getErrors().get(0)));
        }

        @Test
        void createAuthTokenShouldReturnExceptionResponse_whenIncorrectBody() throws Exception {
//        void test_createAuthToken_incorrectBody() throws Exception {
            IncorrectJwtRequest incorrectJwtRequest = new IncorrectJwtRequest("user", "password");
            errors = List.of("Enter username");
            errorValidationResponse = new ErrorValidationResponse(
                    BAD_REQUEST,
                    errors,
                    METHOD_ARGUMENT_NOT_VALID_EXCEPTION_MESSAGE
            );

            mockMvc.perform(post(url)
                            .contentType(APPLICATION_JSON)
                            .content(mapper.writeValueAsString(incorrectJwtRequest)))
                    .andExpect(status().isBadRequest())

//                    .andExpect(jsonPath("$.errors[0].defaultMessage", is("Enter username")));
                    .andExpectAll(
                            jsonPath("$.timestamp").isNotEmpty(),
                            jsonPath("$.status").value(errorValidationResponse.getStatus()),
                            jsonPath("$.message").value(errorValidationResponse.getMessage()),
                            jsonPath("$.errors.[0]").value(errorValidationResponse.getErrors().get(0)));
//                    .andExpect(content().json(mapper.writeValueAsString(errorValidationResponse)));
        }

        @Test
        void createAuthTokenShouldReturnExceptionResponse_whenBadCredentials() throws Exception {
//        void test_createAuthToken_badCredentials() throws Exception {
            BadCredentialsException exception = new BadCredentialsException("it does not matter");
            response = getExceptionResponse(
                    UNAUTHORIZED,
                    BAD_CREDENTIALS_EXCEPTION_MESSAGE,
                    exception
            );

            when(authService.getToken(any()))
                    .thenThrow(exception);

            mockMvc.perform(post(url)
                            .contentType(APPLICATION_JSON)
                            .content(mapper.writeValueAsString(jwtRequest)))
                    .andExpect(status().isUnauthorized())
                    .andExpectAll(
                            jsonPath("$.timestamp").isNotEmpty(),
                            jsonPath("$.status").value(response.getStatus()),
                            jsonPath("$.message").value(response.getMessage()),
                            jsonPath("$.type").value(response.getType()));
//                    .andExpect(content().json(mapper.writeValueAsString(response)));
        }

        @Test
        void createAuthTokenShouldReturnExceptionResponse_whenDbError() throws Exception {
//        void test_createAuthToken_dBError() throws Exception {
            DataSourceLookupFailureException exception = new DataSourceLookupFailureException("it does not matter");
            response = getExceptionResponse(
                    INTERNAL_SERVER_ERROR,
                    DATA_SOURCE_LOOKUP_FAILURE_EXCEPTION_MESSAGE,
                    exception
            );

            when(authService.getToken(any()))
                    .thenThrow(exception);

            mockMvc.perform(post(url)
                            .contentType(APPLICATION_JSON)
                            .content(mapper.writeValueAsString(jwtRequest)))
                    .andExpect(status().isInternalServerError())
                    .andExpectAll(
                            jsonPath("$.timestamp").isNotEmpty(),
                            jsonPath("$.status").value(response.getStatus()),
                            jsonPath("$.message").value(response.getMessage()),
                            jsonPath("$.type").value(response.getType()));
        }

        @Test
        void createAuthTokenShouldReturnExceptionResponse_incorrectBodyType() throws Exception {
//        void test_createAuthToken_incorrectBodyType() throws Exception {
            String request = "user, password";
            HttpMessageNotReadableException exception = new HttpMessageNotReadableException("it does not matter", new MockHttpInputMessage("it does not matter".getBytes()));
            response = getExceptionResponse(
                    INTERNAL_SERVER_ERROR,
                    HTTP_MESSAGE_NOT_READABLE_EXCEPTION_MESSAGE,
                    exception
            );

            mockMvc.perform(post(url)
                            .contentType(APPLICATION_JSON)
                            .content(mapper.writeValueAsString(request)))
                    .andExpect(status().isInternalServerError())
                    .andExpectAll(
                            jsonPath("$.timestamp").isNotEmpty(),
                            jsonPath("$.status").value(response.getStatus()),
                            jsonPath("$.message").value(response.getMessage()),
                            jsonPath("$.type").value(response.getType()));
        }

        record IncorrectJwtRequest(String name, String password) {
        }
    }

    @Nested
    class TestCreateUser {

        private final String url;
        private final UserRegistrationDto userRegistrationDto;

        {
            url = "/auth/registration";
            userRegistrationDto = new UserRegistrationDto();
            userRegistrationDto.setUsername("user");
            userRegistrationDto.setPassword("password");
            userRegistrationDto.setVerifyPassword("password");
        }

        @Test
        void createNewUserShouldReturnSuccessResponse_whenCalled() throws Exception {
//        void test_createNewUser_success() throws Exception {
            doNothing().when(userService)
                    .save(any());

            mockMvc.perform(post(url)
                            .contentType(APPLICATION_JSON)
                            .content(mapper.writeValueAsString(userRegistrationDto)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("message").value(String.format(CREATION_MESSAGE, "user")));
        }

        @Test
        void createNewUserShouldReturnExceptionResponse_whenDbError() throws Exception {
//        void test_createNewUser_dBError() throws Exception {
            DataSourceLookupFailureException exception = new DataSourceLookupFailureException("it does not matter");
            response = getExceptionResponse(
                    INTERNAL_SERVER_ERROR,
                    DATA_SOURCE_LOOKUP_FAILURE_EXCEPTION_MESSAGE,
                    exception
            );

            doThrow(exception).when(userService).save(any());

            mockMvc.perform(post(url)
                            .contentType(APPLICATION_JSON)
                            .content(mapper.writeValueAsString(userRegistrationDto)))
                    .andExpect(status().isInternalServerError())
                    .andExpectAll(
                            jsonPath("$.timestamp").isNotEmpty(),
                            jsonPath("$.status").value(response.getStatus()),
                            jsonPath("$.message").value(response.getMessage()),
                            jsonPath("$.type").value(response.getType()));
        }

        @Test
        void createNewUserShouldReturnExceptionResponse_whenIncorrectBody() throws Exception {
//        void test_createNewUser_incorrectBody() throws Exception {
            IncorrectRegistrationBody incorrectRegistrationBody = new IncorrectRegistrationBody("test", "ester", "password");
            errors = List.of("Verify password", "The entered passwords do not match", "Enter username");
            errorValidationResponse = new ErrorValidationResponse(
                    BAD_REQUEST,
                    errors,
                    METHOD_ARGUMENT_NOT_VALID_EXCEPTION_MESSAGE
            );

            mockMvc.perform(post(url)
                            .contentType(APPLICATION_JSON)
                            .content(mapper.writeValueAsString(incorrectRegistrationBody)))
                    .andExpect(status().isBadRequest())
                    .andExpectAll(
                            jsonPath("$.timestamp").isNotEmpty(),
                            jsonPath("$.status").value(errorValidationResponse.getStatus()),
                            jsonPath("$.message").value(errorValidationResponse.getMessage()),
                            jsonPath("$.errors.size()").value(errorValidationResponse.getErrors().size()));
//                    .andExpect(content().json(mapper.writeValueAsString(errorValidationResponse)));
        }

        @Test
        void createNewUserShouldReturnExceptionResponse_whenPasswordsNotMatching() throws Exception {
//        void test_createNewUser_passwordsNotMatching() throws Exception {
            userRegistrationDto.setVerifyPassword("incorrect");
            errors = List.of("The entered passwords do not match");
            errorValidationResponse = new ErrorValidationResponse(
                    BAD_REQUEST,
                    errors,
                    METHOD_ARGUMENT_NOT_VALID_EXCEPTION_MESSAGE
            );

            mockMvc.perform(post(url)
                            .contentType(APPLICATION_JSON)
                            .content(mapper.writeValueAsString(userRegistrationDto)))
                    .andExpect(status().isBadRequest())
                    .andExpectAll(
                            jsonPath("$.timestamp").isNotEmpty(),
                            jsonPath("$.status").value(errorValidationResponse.getStatus()),
                            jsonPath("$.message").value(errorValidationResponse.getMessage()),
                            jsonPath("$.errors.[0]").value(errorValidationResponse.getErrors().get(0)));
//                    .andExpect(content().json(mapper.writeValueAsString(errorValidationResponse)));
        }

        @Test
        void createNewUserShouldReturnExceptionResponse_whenIncorrectBodyType() throws Exception {
//        void test_createNewUser_incorrectBodyType() throws Exception {
            String request = "user, password, password";
            HttpMessageNotReadableException exception = new HttpMessageNotReadableException("it does not matter", new MockHttpInputMessage("it does not matter".getBytes()));
            response = getExceptionResponse(
                    INTERNAL_SERVER_ERROR,
                    HTTP_MESSAGE_NOT_READABLE_EXCEPTION_MESSAGE,
                    exception
            );

            mockMvc.perform(post(url)
                            .contentType(APPLICATION_JSON)
                            .content(mapper.writeValueAsString(request)))
                    .andExpect(status().isInternalServerError())
                    .andExpectAll(
                            jsonPath("$.timestamp").isNotEmpty(),
                            jsonPath("$.status").value(response.getStatus()),
                            jsonPath("$.message").value(response.getMessage()),
                            jsonPath("$.type").value(response.getType()));
//                    .andExpect(content().json(mapper.writeValueAsString(response)));
        }

        record IncorrectRegistrationBody(String name, String surname, String password) {
        }
    }
}
