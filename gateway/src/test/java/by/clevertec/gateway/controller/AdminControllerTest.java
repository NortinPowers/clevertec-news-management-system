package by.clevertec.gateway.controller;

import static by.clevertec.utils.Constants.USER;
import static by.clevertec.utils.ResponseUtils.CHANGE_ROLE_MESSAGE;
import static by.clevertec.utils.ResponseUtils.CREATION_MESSAGE;
import static by.clevertec.utils.ResponseUtils.getSuccessResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import by.clevertec.auth.UserDto;
import by.clevertec.gateway.util.UserTestBuilder;
import by.clevertec.message.BaseResponse;
import by.clevertec.message.MessageResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@WireMockTest
@ActiveProfiles("test")
@EnableFeignClients
//@EnableConfigurationProperties
@RequiredArgsConstructor
class AdminControllerTest {

    private final AdminController adminController;

    private final ObjectMapper mapper = new ObjectMapper();

    @RegisterExtension
    static WireMockExtension wireMockExtension = WireMockExtension.newInstance()
            .options(wireMockConfig().port(9003).bindAddress("127.0.0.1"))
//            .options(wireMockConfig().dynamicPort().dynamicHttpsPort())
            .build();

//    @DynamicPropertySource
//    public static void setUpMockBaseUrl(DynamicPropertyRegistry registry) {
//        registry.add("http://localhost:8003/admin", wireMockExtension::baseUrl);
//    }


    @Nested
    class TestGetAll {

        @Test
        @WithMockUser(username = "admin", roles = "ADMIN")
        public void getAllShouldReturnEmptyBody_whenAccessAllowedAndNoContent() {
            List<UserDto> userDtos = Collections.emptyList();

            wireMockExtension.stubFor(get(urlEqualTo("/admin/users"))
                    .willReturn(aResponse()
                            .withStatus(OK.value())
                            .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                            .withBody("[]")));

            ResponseEntity<List<UserDto>> actualResponse = adminController.getAllUsers();

            assertThat(actualResponse.getStatusCode()).isEqualTo(OK);
            assertThat(actualResponse.getBody()).isEqualTo(userDtos);
        }

        @Test
        @SneakyThrows
        @WithMockUser(username = "admin", roles = "ADMIN")
        public void getAllShouldReturnNotEmptyBody_whenAccessAllowedAndContentNotEmpty() {
            UserDto userDto = UserTestBuilder.builder()
                    .build()
                    .buildUserDto();

            List<UserDto> userDtos = List.of(userDto);

            wireMockExtension.stubFor(get(urlEqualTo("/admin/users"))
                    .willReturn(aResponse()
                            .withStatus(OK.value())
                            .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                            .withBody(mapper.writeValueAsString(userDtos))));

            ResponseEntity<List<UserDto>> actualResponse = adminController.getAllUsers();

            assertThat(actualResponse.getStatusCode()).isEqualTo(OK);
            assertThat(actualResponse.getBody()).isEqualTo(userDtos);
        }

        @Test
        @WithMockUser(username = "user", roles = "JOURNALIST")
        public void getAllShouldReturnThrowAccessDeniedException_whenAccessDeniedRoleJournalist() {
//            wireMockExtension.stubFor(get(urlEqualTo("/admin/users"))
//                    .willReturn(aResponse()
//                            .withStatus(FORBIDDEN.value())));

            assertThrows(AccessDeniedException.class, adminController::getAllUsers);
        }


        @Test
        @WithMockUser(username = "user", roles = "SUBSCRIBER")
        public void getAllShouldReturnThrowAccessDeniedException_whenAccessDeniedRoleSubscriber() {
//            wireMockExtension.stubFor(get(urlEqualTo("/admin/users"))
//                    .willReturn(aResponse()
//                            .withStatus(FORBIDDEN.value())));

            assertThrows(AccessDeniedException.class, adminController::getAllUsers);
        }
    }


    @Nested
    class TestSetAdmin {

        //TODO

        @Test
        @SneakyThrows
        @WithMockUser(username = "admin", roles = "ADMIN")
        public void setAdminShouldReturnSuccessResponse_whenAccessAllowed() {
            MessageResponse successResponse = getSuccessResponse(CHANGE_ROLE_MESSAGE, USER);

            wireMockExtension.stubFor(post(urlEqualTo("/admin/set/1"))
                    .willReturn(aResponse()
                            .withStatus(OK.value())
                            .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                            .withBody(mapper.writeValueAsString(successResponse))));

            ResponseEntity<BaseResponse> actualResponse = adminController.setAdmin(1L);

            MessageResponse actualBody = (MessageResponse) actualResponse.getBody();
            assertThat(actualResponse.getStatusCode()).isEqualTo(OK);
            assertThat(actualBody.getMessage()).isEqualTo(successResponse.getMessage());
        }

        @Test
        @WithMockUser(username = "user", roles = {"JOURNALIST", "SUBSCRIBER"})
        public void setAdminShouldThrowAccessDeniedException_whenAccessDeniedRoleJournalist() {
//            wireMockExtension.stubFor(post(urlEqualTo("/admin/set/1"))
//                    .willReturn(aResponse()
//                            .withStatus(FORBIDDEN.value())));

            assertThrows(AccessDeniedException.class, adminController::getAllUsers);
        }


//        @Test
//        @WithMockUser(username = "user", roles = "SUBSCRIBER")
//        public void setAdminShouldThrowAccessDeniedException_whenAccessDeniedRoleSubscriber() {
//            wireMockExtension.stubFor(post(urlEqualTo("/admin/set/1"))
//                    .willReturn(aResponse()
//                            .withStatus(FORBIDDEN.value())));
//
//            assertThrows(AccessDeniedException.class, adminController::getAllUsers);
//        }
        @Test
        @WithAnonymousUser
        public void setAdminShouldThrowAccessDeniedException_whenAccessDeniedAnonymousUser() {
//            wireMockExtension.stubFor(post(urlEqualTo("/admin/set/1"))
//                    .willReturn(aResponse()
//                            .withStatus(FORBIDDEN.value())));

            assertThrows(AccessDeniedException.class, adminController::getAllUsers);
        }

    }

}