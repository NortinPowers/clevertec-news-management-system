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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import by.clevertec.gateway.projection.NewsWithCommentsProjection;
import by.clevertec.gateway.util.CommentTestBuilder;
import by.clevertec.gateway.util.CustomPageImplSerializer;
import by.clevertec.gateway.util.NewsTestBuilder;
import by.clevertec.message.BaseResponse;
import by.clevertec.message.MessageResponse;
import by.clevertec.request.NewsPathRequestDto;
import by.clevertec.request.NewsRequestDto;
import by.clevertec.response.CommentResponseDto;
import by.clevertec.response.NewsResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import feign.FeignException.NotFound;
import java.time.LocalDateTime;
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

@WireMockTest
@EnableFeignClients
@ActiveProfiles("test")
@RequiredArgsConstructor
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class NewsControllerTest {

    @RegisterExtension
    static WireMockExtension wireMockExtension = WireMockExtension.newInstance()
            .options(wireMockConfig().port(9001).bindAddress("127.0.0.1"))
            .build();

    private final NewsController newsController;
    private final ObjectMapper mapper = new ObjectMapper();
    private final PageRequest pageRequest = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);

    @Test
    @SneakyThrows
    void getAllShouldReturnPageNewsResponseDtos_whenCalled() {
        NewsResponseDto news = NewsTestBuilder.builder()
                .build()
                .buildNewsResponseDto();
        List<NewsResponseDto> expectedContent = List.of(news);
        Page<NewsResponseDto> dtoPage = new PageImpl<>(expectedContent);

        SimpleModule module = new SimpleModule();
        module.addSerializer(PageImpl.class, new CustomPageImplSerializer());
        mapper.registerModule(module);

        String pageConfig = "?size=15&page=0";

        wireMockExtension.stubFor(get(urlEqualTo("/news" + pageConfig))
                .willReturn(aResponse()
                        .withStatus(OK.value())
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                        .withBody(mapper.writeValueAsString(dtoPage))));

        ResponseEntity<Page<NewsResponseDto>> actualPage = newsController.getAll(pageRequest);

        assertThat(actualPage.getStatusCode()).isEqualTo(OK);
        assertThat(actualPage.getBody().getContent()).isEqualTo(expectedContent);
    }

    @Nested
    class TestGetById {

        @Test
        @SneakyThrows
        void getByIdShouldReturnNewsResponseDto_whenCorrectId() {
            NewsResponseDto news = NewsTestBuilder.builder()
                    .build()
                    .buildNewsResponseDto();

            wireMockExtension.stubFor(get(urlEqualTo("/news/" + CORRECT_ID))
                    .willReturn(aResponse()
                            .withStatus(OK.value())
                            .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                            .withBody(mapper.writeValueAsString(news))));

            ResponseEntity<NewsResponseDto> actual = newsController.getById(CORRECT_ID);

            assertThat(actual.getStatusCode()).isEqualTo(OK);
            assertThat(actual.getBody()).isEqualTo(news);
        }

        @Test
        void getByIdShouldThrowNotFoundException_whenIncorrectId() {
            wireMockExtension.stubFor(get(urlEqualTo("/news/" + INCORRECT_ID))
                    .willReturn(aResponse()
                            .withStatus(NOT_FOUND.value())));

            Executable executable = () -> newsController.getById(INCORRECT_ID);

            assertThrows(NotFound.class, executable);
        }

    }

    @Nested
    class TestSave {

        @Test
        @SneakyThrows
        @WithMockUser(username = "user", roles = {"ADMIN", "JOURNALIST"})
        void saveShouldReturnSuccessResponse_whenCorrectBodyAndAccessAllowed() {
            NewsRequestDto requestDto = NewsTestBuilder.builder()
                    .build()
                    .buildNewsRequestDto();
            MessageResponse successResponse = getSuccessResponse(CREATION_MESSAGE, "news");

            wireMockExtension.stubFor(post(urlEqualTo("/news"))
                    .willReturn(aResponse()
                            .withStatus(OK.value())
                            .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                            .withBody(mapper.writeValueAsString(successResponse))));

            ResponseEntity<BaseResponse> actual = newsController.save(requestDto);

            MessageResponse actualBody = (MessageResponse) actual.getBody();
            assertThat(actual.getStatusCode()).isEqualTo(OK);
            assertThat(actualBody.getMessage()).isEqualTo(successResponse.getMessage());
        }

        @Test
        @WithMockUser(username = "user", roles = "SUBSCRIBER")
        void saveShouldThrowAccessDeniedException_whenAccessDeniedRoleSubscriber() {
            NewsRequestDto requestDto = NewsTestBuilder.builder()
                    .build()
                    .buildNewsRequestDto();

            Executable executable = () -> newsController.save(requestDto);

            assertThrows(AccessDeniedException.class, executable);
        }

        @Test
        @WithAnonymousUser
        void saveShouldThrowAccessDeniedException_whenAccessDeniedAnonymousUser() {
            NewsRequestDto requestDto = NewsTestBuilder.builder()
                    .build()
                    .buildNewsRequestDto();

            Executable executable = () -> newsController.save(requestDto);

            assertThrows(AccessDeniedException.class, executable);
        }
    }

    @Nested
    class TestUpdate {

        private final NewsRequestDto requestDto;

        {
            requestDto = NewsTestBuilder.builder()
                    .build()
                    .buildNewsRequestDto();
        }

        @Test
        @SneakyThrows
        @WithMockUser(username = "user", roles = {"ADMIN", "JOURNALIST"})
        void updateShouldReturnSuccessResponse_whenCorrectBodyAndAccessAllowed() {
            MessageResponse successResponse = getSuccessResponse(UPDATE_MESSAGE, "news");

            wireMockExtension.stubFor(put(urlEqualTo("/news/" + CORRECT_ID))
                    .willReturn(aResponse()
                            .withStatus(OK.value())
                            .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                            .withBody(mapper.writeValueAsString(successResponse))));

            ResponseEntity<BaseResponse> actual = newsController.update(CORRECT_ID, requestDto);

            MessageResponse actualBody = (MessageResponse) actual.getBody();
            assertThat(actual.getStatusCode()).isEqualTo(OK);
            assertThat(actualBody.getMessage()).isEqualTo(successResponse.getMessage());
        }

        @Test
        @WithMockUser(username = "user", roles = "SUBSCRIBER")
        void updateShouldThrowAccessDeniedException_whenAccessDeniedRoleSubscriber() {
            Executable executable = () -> newsController.update(CORRECT_ID, requestDto);

            assertThrows(AccessDeniedException.class, executable);
        }

        @Test
        @WithAnonymousUser
        void updateShouldThrowAccessDeniedException_whenAccessDeniedAnonymousUser() {
            Executable executable = () -> newsController.update(CORRECT_ID, requestDto);

            assertThrows(AccessDeniedException.class, executable);
        }
    }

    @Nested
    class TestUpdatePath {

        private final NewsPathRequestDto requestDto;

        {
            requestDto = NewsTestBuilder.builder()
                    .build()
                    .buildNewsPathRequestDto();
        }

        @Test
        @SneakyThrows
        @WithMockUser(username = "user", roles = {"ADMIN", "JOURNALIST"})
        void updatePathShouldReturnSuccessResponse_whenCorrectBodyAndAccessAllowed() {
            MessageResponse successResponse = getSuccessResponse(UPDATE_MESSAGE, "news");

            wireMockExtension.stubFor(post(urlEqualTo("/news/" + CORRECT_ID))
                    .willReturn(aResponse()
                            .withStatus(OK.value())
                            .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                            .withBody(mapper.writeValueAsString(successResponse))));

            ResponseEntity<BaseResponse> actual = newsController.updatePath(CORRECT_ID, requestDto);

            MessageResponse actualBody = (MessageResponse) actual.getBody();
            assertThat(actual.getStatusCode()).isEqualTo(OK);
            assertThat(actualBody.getMessage()).isEqualTo(successResponse.getMessage());
        }

        @Test
        @WithMockUser(username = "user", roles = "SUBSCRIBER")
        void updatePathShouldThrowAccessDeniedException_whenAccessDeniedRoleSubscriber() {
            Executable executable = () -> newsController.updatePath(CORRECT_ID, requestDto);

            assertThrows(AccessDeniedException.class, executable);
        }

        @Test
        @WithAnonymousUser
        void updatePathShouldThrowAccessDeniedException_whenAccessDeniedAnonymousUser() {
            Executable executable = () -> newsController.updatePath(CORRECT_ID, requestDto);

            assertThrows(AccessDeniedException.class, executable);
        }

        @Test
        @WithMockUser(username = "user", roles = {"ADMIN", "JOURNALIST"})
        void updatePathShouldThrowNotFoundException_whenIncorrectId() {
            wireMockExtension.stubFor(post(urlEqualTo("/news/" + INCORRECT_ID))
                    .willReturn(aResponse()
                            .withStatus(NOT_FOUND.value())));

            Executable executable = () -> newsController.updatePath(INCORRECT_ID, requestDto);

            assertThrows(NotFound.class, executable);
        }
    }

    @Nested
    class TestDelete {

        @Test
        @SneakyThrows
        @WithMockUser(username = "user", roles = {"ADMIN", "JOURNALIST"})
        void deleteShouldReturnSuccessResponse_whenCorrectBodyAndAccessAllowed() {
            MessageResponse successResponse = getSuccessResponse(DELETION_MESSAGE, "news");

            wireMockExtension.stubFor(delete(urlEqualTo("/news/" + CORRECT_ID))
                    .willReturn(aResponse()
                            .withStatus(OK.value())
                            .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                            .withBody(mapper.writeValueAsString(successResponse))));

            ResponseEntity<BaseResponse> actual = newsController.delete(CORRECT_ID);

            MessageResponse actualBody = (MessageResponse) actual.getBody();
            assertThat(actual.getStatusCode()).isEqualTo(OK);
            assertThat(actualBody.getMessage()).isEqualTo(successResponse.getMessage());
        }

        @Test
        @WithMockUser(username = "user", roles = "SUBSCRIBER")
        void deleteShouldThrowAccessDeniedException_whenAccessDeniedRoleSubscriber() {
            Executable executable = () -> newsController.delete(CORRECT_ID);

            assertThrows(AccessDeniedException.class, executable);
        }

        @Test
        @WithAnonymousUser
        void deleteShouldThrowAccessDeniedException_whenAccessDeniedAnonymousUser() {
            Executable executable = () -> newsController.delete(CORRECT_ID);

            assertThrows(AccessDeniedException.class, executable);
        }

        @Test
        @WithMockUser(username = "user", roles = {"ADMIN", "JOURNALIST"})
        void deleteShouldThrowNotFoundException_whenIncorrectId() {
            wireMockExtension.stubFor(delete(urlEqualTo("/news/" + INCORRECT_ID))
                    .willReturn(aResponse()
                            .withStatus(NOT_FOUND.value())));

            Executable executable = () -> newsController.delete(INCORRECT_ID);

            assertThrows(NotFound.class, executable);
        }
    }

    @Nested
    class TestGetCommentsByNewsId {

        @RegisterExtension
        static WireMockExtension wireMockExtensionComments = WireMockExtension.newInstance()
                .options(wireMockConfig().port(9002).bindAddress("127.0.0.1"))
                .build();

        @Test
        @SneakyThrows
        @WithMockUser(username = "user", roles = {"ADMIN", "JOURNALIST", "SUBSCRIBER"})
        void getCommentsByNewsIdShouldReturnPageWithNews_whenCalledAndAuthorizedUser() {
            NewsResponseDto expectedNews = NewsTestBuilder.builder()
                    .build()
                    .buildNewsResponseDto();

            wireMockExtension.stubFor(get(urlEqualTo("/news/" + CORRECT_ID))
                    .willReturn(aResponse()
                            .withStatus(OK.value())
                            .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                            .withBody(mapper.writeValueAsString(expectedNews))));

            CommentResponseDto comment = CommentTestBuilder.builder()
                    .build()
                    .buildCommentResponseDto();
            List<CommentResponseDto> expectedContent = List.of(comment);
            Page<CommentResponseDto> dtoPage = new PageImpl<>(expectedContent);

            SimpleModule module = new SimpleModule();
            module.addSerializer(PageImpl.class, new CustomPageImplSerializer());
            mapper.registerModule(module);

            String pageConfig = "?size=15&page=0";

            wireMockExtensionComments.stubFor(get(urlEqualTo("/comments/news/" + CORRECT_ID + pageConfig))
                    .willReturn(aResponse()
                            .withStatus(OK.value())
                            .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                            .withBody(mapper.writeValueAsString(dtoPage))));

            ResponseEntity<NewsWithCommentsProjection> actualResponse = newsController.getCommentsByNewsId(CORRECT_ID, pageRequest);

            assertThat(actualResponse.getStatusCode()).isEqualTo(OK);
            assertThat(actualResponse.getBody())
                    .hasFieldOrPropertyWithValue("time", LocalDateTime.parse(expectedNews.getTime()))
                    .hasFieldOrPropertyWithValue("title", expectedNews.getTitle())
                    .hasFieldOrPropertyWithValue("text", expectedNews.getText())
                    .hasFieldOrPropertyWithValue("author", expectedNews.getAuthor());
            assertThat(actualResponse.getBody().getComments().getContent()).isEqualTo(expectedContent);
        }

        @Test
        @WithAnonymousUser
        void getCommentsByNewsIdShouldThrowAccessDeniedException_whenAccessDeniedAnonymousUser() {
            Executable executable = () -> newsController.getCommentsByNewsId(CORRECT_ID, pageRequest);

            assertThrows(AccessDeniedException.class, executable);
        }
    }

    @Nested
    class TestGetPersonSearchResult {

        private final String condition = "condition";

        @Test
        @SneakyThrows
        @WithMockUser(username = "user", roles = {"ADMIN", "JOURNALIST", "SUBSCRIBER"})
        void getPersonSearchResultShouldReturnPageNewsResponseDtos_whenCalledAndAuthorizedUser() {
            NewsResponseDto news = NewsTestBuilder.builder()
                    .build()
                    .buildNewsResponseDto();
            List<NewsResponseDto> expectedContent = List.of(news);
            Page<NewsResponseDto> dtoPage = new PageImpl<>(expectedContent);
            String testUrl = "/news/search/";
            String pageConfig = "?size=15&page=0";

            SimpleModule module = new SimpleModule();
            module.addSerializer(PageImpl.class, new CustomPageImplSerializer());
            mapper.registerModule(module);

            wireMockExtension.stubFor(get(urlEqualTo(testUrl + condition + pageConfig))
                    .willReturn(aResponse()
                            .withStatus(OK.value())
                            .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                            .withBody(mapper.writeValueAsString(dtoPage))));

            ResponseEntity<Page<NewsResponseDto>> actualPage = newsController.getPersonSearchResult(condition, pageRequest);

            assertThat(actualPage.getStatusCode()).isEqualTo(OK);
            assertThat(actualPage.getBody().getContent()).isEqualTo(expectedContent);
        }

        @Test
        @WithAnonymousUser
        void getPersonSearchResultShouldThrowAccessDeniedException_whenAccessDeniedAnonymousUser() {
            Executable executable = () -> newsController.getPersonSearchResult(condition, pageRequest);

            assertThrows(AccessDeniedException.class, executable);
        }
    }
}
