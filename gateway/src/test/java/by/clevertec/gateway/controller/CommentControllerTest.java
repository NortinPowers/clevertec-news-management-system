package by.clevertec.gateway.controller;

import static by.clevertec.gateway.util.TestConstant.CORRECT_ID;
import static by.clevertec.gateway.util.TestConstant.INCORRECT_ID;
import static by.clevertec.gateway.util.TestConstant.PAGE_NUMBER;
import static by.clevertec.gateway.util.TestConstant.PAGE_SIZE;
import static by.clevertec.utils.ResponseUtils.CREATION_MESSAGE;
import static by.clevertec.utils.ResponseUtils.DELETION_MESSAGE;
import static by.clevertec.utils.ResponseUtils.UPDATE_MESSAGE;
import static by.clevertec.utils.ResponseUtils.getSuccessResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.delete;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.put;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import by.clevertec.gateway.util.CustomPageImplSerializer;
import by.clevertec.gateway.util.CommentTestBuilder;
import by.clevertec.message.BaseResponse;
import by.clevertec.message.MessageResponse;
import by.clevertec.request.CommentPathRequestDto;
import by.clevertec.request.CommentRequestDto;
import by.clevertec.response.CommentResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import feign.FeignException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.api.function.Executable;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@WireMockTest
@EnableFeignClients
@ActiveProfiles("test")
@RequiredArgsConstructor
class CommentControllerTest {

    private final CommentController commentController;

    private final ObjectMapper mapper = new ObjectMapper();

    private final PageRequest pageRequest = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);


    @RegisterExtension
    static WireMockExtension wireMockExtension = WireMockExtension.newInstance()
            .options(wireMockConfig().port(9002).bindAddress("127.0.0.1"))
            .build();

    @Test
    @SneakyThrows
    void getAllShouldReturnPageCommentResponseDtos_whenCalled() {
        CommentResponseDto comment = CommentTestBuilder.builder()
                .build()
                .buildCommentResponseDto();
        List<CommentResponseDto> expectedContent = List.of(comment);
        Page<CommentResponseDto> dtoPage = new PageImpl<>(expectedContent);

        SimpleModule module = new SimpleModule();
        module.addSerializer(PageImpl.class, new CustomPageImplSerializer());
        mapper.registerModule(module);

        String pageConfig = "?size=15&page=0";

        wireMockExtension.stubFor(get(urlEqualTo("/comments" + pageConfig))
                .willReturn(aResponse()
                        .withStatus(OK.value())
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                        .withBody(mapper.writeValueAsString(dtoPage))));

        ResponseEntity<Page<CommentResponseDto>> actualPage = commentController.getAll(pageRequest);

        assertThat(actualPage.getStatusCode()).isEqualTo(OK);
        assertThat(actualPage.getBody().getContent()).isEqualTo(expectedContent);
    }

    @Nested
    class TestGetById {

        @Test
        @SneakyThrows
        void getByIdShouldReturnCommentResponseDto_whenCorrectId() {
            CommentResponseDto comment = CommentTestBuilder.builder()
                    .build()
                    .buildCommentResponseDto();

            wireMockExtension.stubFor(get(urlEqualTo("/comments/" + CORRECT_ID))
                    .willReturn(aResponse()
                            .withStatus(OK.value())
                            .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                            .withBody(mapper.writeValueAsString(comment))));

            ResponseEntity<CommentResponseDto> actual = commentController.getById(CORRECT_ID);

            assertThat(actual.getStatusCode()).isEqualTo(OK);
            assertThat(actual.getBody()).isEqualTo(comment);
        }

        @Test
        void getByIdShouldThrowNotFoundException_whenIncorrectId() {
            wireMockExtension.stubFor(get(urlEqualTo("/comments/" + INCORRECT_ID))
                    .willReturn(aResponse()
                            .withStatus(NOT_FOUND.value())));

            Executable executable = () -> commentController.getById(INCORRECT_ID);

            assertThrows(FeignException.NotFound.class, executable);
        }

    }

    @Nested
    class TestSave {

        @Test
        @SneakyThrows
        @WithMockUser(username = "user", roles = {"ADMIN", "JOURNALIST"})
        void saveShouldReturnSuccessResponse_whenCorrectBodyAndAccessAllowed() {
            CommentRequestDto requestDto = CommentTestBuilder.builder()
                    .build()
                    .buildCommentRequestDto();
            MessageResponse successResponse = getSuccessResponse(CREATION_MESSAGE, "comment");

            wireMockExtension.stubFor(post(urlEqualTo("/comments"))
                    .willReturn(aResponse()
                            .withStatus(OK.value())
                            .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                            .withBody(mapper.writeValueAsString(successResponse))));

            ResponseEntity<BaseResponse> actual = commentController.save(requestDto);

            MessageResponse actualBody = (MessageResponse) actual.getBody();
            assertThat(actual.getStatusCode()).isEqualTo(OK);
            assertThat(actualBody.getMessage()).isEqualTo(successResponse.getMessage());
        }

        @Test
        @WithMockUser(username = "user", roles = "SUBSCRIBER")
        void saveShouldThrowAccessDeniedException_whenAccessDeniedRoleSubscriber() {
            CommentRequestDto requestDto = CommentTestBuilder.builder()
                    .build()
                    .buildCommentRequestDto();

            Executable executable = () -> commentController.save(requestDto);

            assertThrows(AccessDeniedException.class, executable);
        }

        @Test
        @WithAnonymousUser
        void saveShouldThrowAccessDeniedException_whenAccessDeniedAnonymousUser() {
            CommentRequestDto requestDto = CommentTestBuilder.builder()
                    .build()
                    .buildCommentRequestDto();

            Executable executable = () -> commentController.save(requestDto);

            assertThrows(AccessDeniedException.class, executable);
        }
    }

    @Nested
    class TestUpdate {

        private final CommentRequestDto requestDto;

        {
            requestDto = CommentTestBuilder.builder()
                    .build()
                    .buildCommentRequestDto();
        }

        @Test
        @SneakyThrows
        @WithMockUser(username = "user", roles = {"ADMIN", "JOURNALIST"})
        void updateShouldReturnSuccessResponse_whenCorrectBodyAndAccessAllowed() {
            MessageResponse successResponse = getSuccessResponse(UPDATE_MESSAGE, "comment");

            wireMockExtension.stubFor(put(urlEqualTo("/comments/" + CORRECT_ID))
                    .willReturn(aResponse()
                            .withStatus(OK.value())
                            .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                            .withBody(mapper.writeValueAsString(successResponse))));

            ResponseEntity<BaseResponse> actual = commentController.update(CORRECT_ID, requestDto);

            MessageResponse actualBody = (MessageResponse) actual.getBody();
            assertThat(actual.getStatusCode()).isEqualTo(OK);
            assertThat(actualBody.getMessage()).isEqualTo(successResponse.getMessage());
        }

        @Test
        @WithMockUser(username = "user", roles = "SUBSCRIBER")
        void updateShouldThrowAccessDeniedException_whenAccessDeniedRoleSubscriber() {
            Executable executable = () -> commentController.update(CORRECT_ID, requestDto);

            assertThrows(AccessDeniedException.class, executable);
        }

        @Test
        @WithAnonymousUser
        void updateShouldThrowAccessDeniedException_whenAccessDeniedAnonymousUser() {
            Executable executable = () -> commentController.update(CORRECT_ID, requestDto);

            assertThrows(AccessDeniedException.class, executable);
        }

        //TODO

        @Test
        @WithMockUser(username = "user", roles = {"ADMIN", "JOURNALIST"})
        void updateShouldThrowNotFoundException_whenIncorrectId() {
            wireMockExtension.stubFor(put(urlEqualTo("/comments/" + INCORRECT_ID))
                    .willReturn(aResponse()
                            .withStatus(NOT_FOUND.value())));

            Executable executable = () -> commentController.update(INCORRECT_ID, requestDto);

            assertThrows(FeignException.NotFound.class, executable);
        }

    }


    @Nested
    class TestUpdatePath {

        private final CommentPathRequestDto requestDto;

        {
            requestDto = CommentTestBuilder.builder()
                    .build()
                    .buildCommentPathRequestDto();
        }

        @Test
        @SneakyThrows
        @WithMockUser(username = "user", roles = {"ADMIN", "JOURNALIST"})
        void updatePathShouldReturnSuccessResponse_whenCorrectBodyAndAccessAllowed() {
            MessageResponse successResponse = getSuccessResponse(UPDATE_MESSAGE, "comment");

            wireMockExtension.stubFor(post(urlEqualTo("/comments/" + CORRECT_ID))
                    .willReturn(aResponse()
                            .withStatus(OK.value())
                            .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                            .withBody(mapper.writeValueAsString(successResponse))));

            ResponseEntity<BaseResponse> actual = commentController.updatePath(CORRECT_ID, requestDto);

            MessageResponse actualBody = (MessageResponse) actual.getBody();
            assertThat(actual.getStatusCode()).isEqualTo(OK);
            assertThat(actualBody.getMessage()).isEqualTo(successResponse.getMessage());
        }

        @Test
        @WithMockUser(username = "user", roles = "SUBSCRIBER")
        void updatePathShouldThrowAccessDeniedException_whenAccessDeniedRoleSubscriber() {
            Executable executable = () -> commentController.updatePath(CORRECT_ID, requestDto);

            assertThrows(AccessDeniedException.class, executable);
        }

        @Test
        @WithAnonymousUser
        void updatePathShouldThrowAccessDeniedException_whenAccessDeniedAnonymousUser() {
            Executable executable = () -> commentController.updatePath(CORRECT_ID, requestDto);

            assertThrows(AccessDeniedException.class, executable);
        }

        @Test
        @WithMockUser(username = "user", roles = {"ADMIN", "JOURNALIST"})
        void updatePathShouldThrowNotFoundException_whenIncorrectId() {
            wireMockExtension.stubFor(post(urlEqualTo("/comments/" + INCORRECT_ID))
                    .willReturn(aResponse()
                            .withStatus(NOT_FOUND.value())));

            Executable executable = () -> commentController.updatePath(INCORRECT_ID, requestDto);

            assertThrows(FeignException.NotFound.class, executable);
        }
    }

    @Nested
    class TestDelete {

        @Test
        @SneakyThrows
        @WithMockUser(username = "user", roles = {"ADMIN", "JOURNALIST"})
        void deleteShouldReturnSuccessResponse_whenCorrectBodyAndAccessAllowed() {
            MessageResponse successResponse = getSuccessResponse(DELETION_MESSAGE, "comment");

            wireMockExtension.stubFor(delete(urlEqualTo("/comments/" + CORRECT_ID))
                    .willReturn(aResponse()
                            .withStatus(OK.value())
                            .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                            .withBody(mapper.writeValueAsString(successResponse))));

            ResponseEntity<BaseResponse> actual = commentController.delete(CORRECT_ID);

            MessageResponse actualBody = (MessageResponse) actual.getBody();
            assertThat(actual.getStatusCode()).isEqualTo(OK);
            assertThat(actualBody.getMessage()).isEqualTo(successResponse.getMessage());
        }

        @Test
        @WithMockUser(username = "user", roles = "SUBSCRIBER")
        void deleteShouldThrowAccessDeniedException_whenAccessDeniedRoleSubscriber() {
            Executable executable = () -> commentController.delete(CORRECT_ID);

            assertThrows(AccessDeniedException.class, executable);
        }

        @Test
        @WithAnonymousUser
        void deleteShouldThrowAccessDeniedException_whenAccessDeniedAnonymousUser() {
            Executable executable = () -> commentController.delete(CORRECT_ID);

            assertThrows(AccessDeniedException.class, executable);
        }

        @Test
        @WithMockUser(username = "user", roles = {"ADMIN", "JOURNALIST"})
        void deleteShouldThrowNotFoundException_whenIncorrectId() {
            wireMockExtension.stubFor(delete(urlEqualTo("/comments/" + INCORRECT_ID))
                    .willReturn(aResponse()
                            .withStatus(NOT_FOUND.value())));

            Executable executable = () -> commentController.delete(INCORRECT_ID);

            assertThrows(FeignException.NotFound.class, executable);
        }

    }
}
