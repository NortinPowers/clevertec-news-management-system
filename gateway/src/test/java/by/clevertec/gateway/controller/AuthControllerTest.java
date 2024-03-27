package by.clevertec.gateway.controller;

import static by.clevertec.utils.ResponseUtils.CREATION_MESSAGE;
import static by.clevertec.utils.ResponseUtils.getSuccessResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import by.clevertec.auth.JwtRequest;
import by.clevertec.auth.JwtResponse;
import by.clevertec.auth.UserRegistrationDto;
import by.clevertec.message.BaseResponse;
import by.clevertec.message.MessageResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import feign.FeignException.BadRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.api.function.Executable;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;

@WireMockTest
@EnableFeignClients
@ActiveProfiles("test")
@RequiredArgsConstructor
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthControllerTest {

    @RegisterExtension
    static WireMockExtension wireMockExtension = WireMockExtension.newInstance()
            .options(wireMockConfig().port(9003).bindAddress("127.0.0.1"))
            .build();

    private final AuthController authController;
    private final ObjectMapper mapper = new ObjectMapper();

    @Nested
    class TestCreateAuthToken {

        private final String testUrl = "/auth";

        @Test
        @SneakyThrows
        @WithMockUser(username = "user", roles = {"ADMIN", "JOURNALIST", "SUBSCRIBER"})
        public void createAuthTokenShouldReturnJwtResponse_whenCalledWithCorrectJwtRequestAllRoles() {
            String token = "security";
            JwtRequest request = new JwtRequest();
            request.setUsername("user");
            request.setPassword("password");
            JwtResponse response = new JwtResponse();
            response.setToken(token);

            wireMockExtension.stubFor(post(urlEqualTo(testUrl))
                    .willReturn(aResponse()
                            .withStatus(OK.value())
                            .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                            .withBody(mapper.writeValueAsString(response))));

            ResponseEntity<JwtResponse> actualResponse = authController.createAuthToken(request);

            assertThat(actualResponse.getStatusCode()).isEqualTo(OK);
            assertThat(actualResponse.getBody().getToken()).isEqualTo(response.getToken());
        }

        @Test
        @SneakyThrows
        @WithAnonymousUser
        public void createAuthTokenShouldReturnJwtResponse_whenCalledWithCorrectJwtRequestAnonymous() {
            String token = "security";
            JwtRequest request = new JwtRequest();
            request.setUsername("user");
            request.setPassword("password");
            JwtResponse response = new JwtResponse();
            response.setToken(token);

            wireMockExtension.stubFor(post(urlEqualTo(testUrl))
                    .willReturn(aResponse()
                            .withStatus(OK.value())
                            .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                            .withBody(mapper.writeValueAsString(response))));

            ResponseEntity<JwtResponse> actualResponse = authController.createAuthToken(request);

            assertThat(actualResponse.getStatusCode()).isEqualTo(OK);
            assertThat(actualResponse.getBody().getToken()).isEqualTo(token);
        }

        @Test
        public void createAuthTokenShouldThrowBadRequestException_whenCalledWithIncorrectRequest() {
            JwtRequest request = new JwtRequest();
            request.setUsername("user");
            request.setPassword("password");

            wireMockExtension.stubFor(post(urlEqualTo(testUrl))
                    .willReturn(aResponse()
                            .withStatus(BAD_REQUEST.value())));

            Executable executable = () -> authController.createAuthToken(request);

            assertThrows(BadRequest.class, executable);
        }
    }

    @Nested
    class TestCreateNewUser {

        private final String testUrl = "/auth/registration";

        private final UserRegistrationDto userRegistrationDto;

        {
            userRegistrationDto = new UserRegistrationDto();
            userRegistrationDto.setUsername("name");
            userRegistrationDto.setPassword("password");
            userRegistrationDto.setVerifyPassword("password");
        }

        @Test
        @SneakyThrows
        public void createNewUserShouldReturnSuccessResponse_whenCalledWithCorrectBody() {
            MessageResponse response = getSuccessResponse(CREATION_MESSAGE, "user");

            wireMockExtension.stubFor(post(urlEqualTo(testUrl))
                    .willReturn(aResponse()
                            .withStatus(OK.value())
                            .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                            .withBody(mapper.writeValueAsString(response))));

            ResponseEntity<BaseResponse> actualResponse = authController.createNewUser(userRegistrationDto);

            MessageResponse actualBody = (MessageResponse) actualResponse.getBody();
            assertThat(actualResponse.getStatusCode()).isEqualTo(OK);
            assertThat(actualBody.getMessage()).isEqualTo(response.getMessage());
        }

        @Test
        public void createNewUserShouldThrowBadRequestException_whenIncorrectBody() {
            userRegistrationDto.setVerifyPassword("any password");

            wireMockExtension.stubFor(post(urlEqualTo(testUrl))
                    .willReturn(aResponse()
                            .withStatus(BAD_REQUEST.value())));

            Executable executable = () -> authController.createNewUser(userRegistrationDto);

            assertThrows(BadRequest.class, executable);
        }
    }
}
