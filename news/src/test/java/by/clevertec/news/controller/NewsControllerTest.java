package by.clevertec.news.controller;

import static by.clevertec.news.util.TestConstant.AUTHOR_NAME;
import static by.clevertec.news.util.TestConstant.CORRECT_ID;
import static by.clevertec.news.util.TestConstant.INCORRECT_ID;
import static by.clevertec.news.util.TestConstant.NEWS_ID;
import static by.clevertec.util.ResponseUtils.HTTP_NOT_READABLE_EXCEPTION_MESSAGE;
import static by.clevertec.util.ResponseUtils.METHOD_ARGUMENT_NOT_VALID_EXCEPTION_MESSAGE;
import static by.clevertec.util.ResponseUtils.getExceptionResponse;
import static by.clevertec.utils.ResponseUtils.CREATION_MESSAGE;
import static by.clevertec.utils.ResponseUtils.DELETION_MESSAGE;
import static by.clevertec.utils.ResponseUtils.UPDATE_MESSAGE;
import static by.clevertec.utils.ResponseUtils.getSuccessResponse;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import by.clevertec.exception.CustomAccessException;
import by.clevertec.exception.CustomEntityNotFoundException;
import by.clevertec.exception.CustomNoContentException;
import by.clevertec.message.MessageResponse;
import by.clevertec.model.ErrorValidationResponse;
import by.clevertec.model.ExceptionResponse;
import by.clevertec.news.config.TestContainerConfig;
import by.clevertec.news.domain.News;
import by.clevertec.news.proxy.NewsCacheableAspect;
import by.clevertec.news.util.NewsTestBuilder;
import by.clevertec.request.NewsAndNamePathRequestDto;
import by.clevertec.request.NewsAndNameRequestDto;
import by.clevertec.request.NewsRequestDto;
import by.clevertec.response.NewsResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.mock.http.MockHttpInputMessage;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

//@SpringBootTest
@ActiveProfiles("test")
@Transactional
@AutoConfigureMockMvc
@RequiredArgsConstructor
@SpringBootTest(classes = TestContainerConfig.class)
//TODO @Transactional не работает!!
@Sql(value = "classpath:sql/insert-before.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)

@Sql(value = "classpath:sql/reset-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class NewsControllerTest {

    private final ObjectMapper mapper;
    private final MockMvc mockMvc;
    private final NewsCacheableAspect cacheableAspect;

    @Nested
    class TestGetAll {

        private final String url = "/news";

        @Test
        void getAllShouldReturnPageWithNewsResponseDtosList() throws Exception {
            NewsResponseDto news = NewsTestBuilder.builder()
                    .build()
                    .buildNewsResponseDto();

            mockMvc.perform(get(url))
                    .andExpect(status().isOk())
                    .andExpectAll(
                            jsonPath("$.content").isArray(),
                            jsonPath("$.content[0].time").value(news.getTime()),
                            jsonPath("$.content[0].title").value(news.getTitle()),
                            jsonPath("$.content[0].text").value(news.getText()),
                            jsonPath("$.content[0].author").value(news.getAuthor()));
        }

        @Test

        //TODO

        @Sql(value = "classpath:sql/reset-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
        void getAllShouldReturnExceptionResponse_whenNewsListIsEmpty() throws Exception {
            CustomNoContentException exception = CustomNoContentException.of(News.class);
            ExceptionResponse response = getExceptionResponse(
                    HttpStatus.GONE,
                    exception.getMessage(),
                    exception
            );

            mockMvc.perform(get(url))
                    .andExpect(status().isGone())
                    .andExpectAll(
                            jsonPath("$.timestamp").isNotEmpty(),
                            jsonPath("$.status").value(response.getStatus()),
                            jsonPath("$.message").value(response.getMessage()),
                            jsonPath("$.type").value(response.getType()));
        }
    }

    @Nested
    class TestGetById {

        @Test
        void getByIdShouldReturnNewsResponseDto() throws Exception {
            NewsResponseDto news = NewsTestBuilder.builder()
                    .build()
                    .buildNewsResponseDto();
            cacheableAspect.cacheableDelete(NEWS_ID);
            String url = "/news/" + NEWS_ID;

            mockMvc.perform(get(url))
                    .andExpect(status().isOk())
                    .andExpect(content().json(mapper.writeValueAsString(news)));
        }

        @Test
        void getByIdShouldReturnExceptionResponse_whenIncorrectId() throws Exception {
            CustomEntityNotFoundException exception = CustomEntityNotFoundException.of(News.class, INCORRECT_ID);
            ExceptionResponse response = getExceptionResponse(
                    HttpStatus.NOT_FOUND,
                    exception.getMessage(),
                    exception
            );
            String url = "/news/" + INCORRECT_ID;

            mockMvc.perform(get(url))
                    .andExpect(status().isNotFound())
                    .andExpectAll(
                            jsonPath("$.timestamp").isNotEmpty(),
                            jsonPath("$.status").value(response.getStatus()),
                            jsonPath("$.message").value(response.getMessage()),
                            jsonPath("$.type").value(response.getType()));
        }
    }

    @Nested
    class TestSave {

        private final String url = "/news";

        @Test
        void saveShouldReturnSuccessResponse_whenValidRequestSend() throws Exception {
            NewsAndNameRequestDto requestDto = NewsTestBuilder.builder()
                    .build()
                    .buildNewsAndNameRequestDto();
            MessageResponse response = getSuccessResponse(CREATION_MESSAGE, News.class);

            mockMvc.perform(post(url)
                            .contentType(APPLICATION_JSON)
                            .content(mapper.writeValueAsString(requestDto)))
                    .andExpect(status().isOk())
                    .andExpectAll(
                            jsonPath("$.timestamp").isNotEmpty(),
                            jsonPath("$.status").value(response.getStatus()),
                            jsonPath("$.message").value(response.getMessage()));
        }

        @Test
        void saveShouldReturnExceptionResponse_whenInvalidRequestBodySend() throws Exception {
            String requestDto = "some value";
            HttpMessageNotReadableException exception = new HttpMessageNotReadableException("not matter", new MockHttpInputMessage("not matter".getBytes()));
            ExceptionResponse response = getExceptionResponse(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    HTTP_NOT_READABLE_EXCEPTION_MESSAGE,
                    exception);

            mockMvc.perform(post(url)
                            .contentType(APPLICATION_JSON)
                            .content(mapper.writeValueAsString(requestDto)))
                    .andExpect(status().isInternalServerError())
                    .andExpectAll(
                            jsonPath("$.timestamp").isNotEmpty(),
                            jsonPath("$.status").value(response.getStatus()),
                            jsonPath("$.message").value(response.getMessage()),
                            jsonPath("$.type").value(response.getType()));
        }
    }

    @Nested
    class TestPathUpdate {

        private final String url = "/news/{id}";
        private final NewsAndNamePathRequestDto correctRequestDto = NewsTestBuilder.builder()
                .build()
                .buildNewsAndNamePathRequestDto();

        @Test
        void updateShouldReturnSuccessResponse_whenValidRequestSend() throws Exception {
            MessageResponse response = getSuccessResponse(UPDATE_MESSAGE, News.class);

            mockMvc.perform(post(url, CORRECT_ID)
                            .contentType(APPLICATION_JSON)
                            .content(mapper.writeValueAsString(correctRequestDto)))
                    .andExpect(status().isOk())
                    .andExpectAll(
                            jsonPath("$.timestamp").isNotEmpty(),
                            jsonPath("$.status").value(response.getStatus()),
                            jsonPath("$.message").value(response.getMessage()));
        }

        @Test
        void updateShouldReturnExceptionResponse_whenIncorrectId() throws Exception {
            CustomEntityNotFoundException exception = CustomEntityNotFoundException.of(News.class, INCORRECT_ID);
            ExceptionResponse response = getExceptionResponse(
                    HttpStatus.NOT_FOUND,
                    exception.getMessage(),
                    exception
            );

            mockMvc.perform(post(url, INCORRECT_ID)
                            .contentType(APPLICATION_JSON)
                            .content(mapper.writeValueAsString(correctRequestDto)))
                    .andExpect(status().isNotFound())
                    .andExpectAll(
                            jsonPath("$.timestamp").isNotEmpty(),
                            jsonPath("$.status").value(response.getStatus()),
                            jsonPath("$.message").value(response.getMessage()),
                            jsonPath("$.type").value(response.getType()));
        }

        @Test
        void updateShouldReturnExceptionResponse_whenInvalidRequestBodySend() throws Exception {
            String requestDto = "some value";
            HttpMessageNotReadableException exception = new HttpMessageNotReadableException("not matter", new MockHttpInputMessage("not matter".getBytes()));
            ExceptionResponse response = getExceptionResponse(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    HTTP_NOT_READABLE_EXCEPTION_MESSAGE,
                    exception);

            mockMvc.perform(post(url, CORRECT_ID)
                            .contentType(APPLICATION_JSON)
                            .content(mapper.writeValueAsString(requestDto)))
                    .andExpect(status().isInternalServerError())
                    .andExpectAll(
                            jsonPath("$.timestamp").isNotEmpty(),
                            jsonPath("$.status").value(response.getStatus()),
                            jsonPath("$.message").value(response.getMessage()),
                            jsonPath("$.type").value(response.getType()));
        }

        @Test
        void updateShouldReturnExceptionResponse_whenHasNotRightToModify() throws Exception {
            NewsAndNamePathRequestDto requestDto = NewsTestBuilder.builder()
                    .build()
                    .buildNewsAndNamePathRequestDto();
            requestDto.setName("Someone else");
            CustomAccessException exception = CustomAccessException.of(News.class);
            ExceptionResponse response = getExceptionResponse(
                    HttpStatus.FORBIDDEN,
                    exception.getMessage(),
                    exception
            );

            mockMvc.perform(post(url, CORRECT_ID)
                            .contentType(APPLICATION_JSON)
                            .content(mapper.writeValueAsString(requestDto)))
                    .andExpect(status().isForbidden())
                    .andExpectAll(
                            jsonPath("$.timestamp").isNotEmpty(),
                            jsonPath("$.status").value(response.getStatus()),
                            jsonPath("$.message").value(response.getMessage()),
                            jsonPath("$.type").value(response.getType()));
        }
    }

    @Nested
    class TestUpdate {

        private final String url = "/news/{id}";
        private final NewsAndNameRequestDto correctRequestDto = NewsTestBuilder.builder()
                .build()
                .buildNewsAndNameRequestDto();

        @Test
        void updateShouldReturnSuccessResponse_whenValidRequestSend() throws Exception {
            MessageResponse response = getSuccessResponse(UPDATE_MESSAGE, News.class);

            mockMvc.perform(put(url, CORRECT_ID)
                            .contentType(APPLICATION_JSON)
                            .content(mapper.writeValueAsString(correctRequestDto)))
                    .andExpect(status().isOk())
                    .andExpectAll(
                            jsonPath("$.timestamp").isNotEmpty(),
                            jsonPath("$.status").value(response.getStatus()),
                            jsonPath("$.message").value(response.getMessage()));
        }

        @Test
        void updateShouldReturnExceptionResponse_whenIncorrectId() throws Exception {
            CustomEntityNotFoundException exception = CustomEntityNotFoundException.of(News.class, INCORRECT_ID);
            ExceptionResponse response = getExceptionResponse(
                    HttpStatus.NOT_FOUND,
                    exception.getMessage(),
                    exception
            );

            mockMvc.perform(put(url, INCORRECT_ID)
                            .contentType(APPLICATION_JSON)
                            .content(mapper.writeValueAsString(correctRequestDto)))
                    .andExpect(status().isNotFound())
                    .andExpectAll(
                            jsonPath("$.timestamp").isNotEmpty(),
                            jsonPath("$.status").value(response.getStatus()),
                            jsonPath("$.message").value(response.getMessage()),
                            jsonPath("$.type").value(response.getType()));
        }

        @Test
        void updateShouldReturnExceptionResponse_whenInvalidRequestBodySend() throws Exception {
            String requestDto = "some value";
            HttpMessageNotReadableException exception = new HttpMessageNotReadableException("not matter", new MockHttpInputMessage("not matter".getBytes()));
            ExceptionResponse response = getExceptionResponse(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    HTTP_NOT_READABLE_EXCEPTION_MESSAGE,
                    exception);

            mockMvc.perform(put(url, CORRECT_ID)
                            .contentType(APPLICATION_JSON)
                            .content(mapper.writeValueAsString(requestDto)))
                    .andExpect(status().isInternalServerError())
                    .andExpectAll(
                            jsonPath("$.timestamp").isNotEmpty(),
                            jsonPath("$.status").value(response.getStatus()),
                            jsonPath("$.message").value(response.getMessage()),
                            jsonPath("$.type").value(response.getType()));
        }

        @Test
        void updateShouldReturnExceptionResponse_whenInvalidValueInRequestBodySend() throws Exception {
            NewsRequestDto newsRequestDto = NewsTestBuilder.builder()
                    .build()
                    .buildNewsRequestDto();
            NewsAndNameRequestDto requestDto = new NewsAndNameRequestDto();
            requestDto.setRequestDto(newsRequestDto);
            List<String> errors = List.of("Enter name");
            ErrorValidationResponse response = new ErrorValidationResponse(
                    HttpStatus.BAD_REQUEST,
                    errors,
                    METHOD_ARGUMENT_NOT_VALID_EXCEPTION_MESSAGE);

            mockMvc.perform(put(url, CORRECT_ID)
                            .contentType(APPLICATION_JSON)
                            .content(mapper.writeValueAsString(requestDto)))
                    .andExpect(status().isBadRequest())
                    .andExpectAll(
                            jsonPath("$.timestamp").isNotEmpty(),
                            jsonPath("$.status").value(response.getStatus()),
                            jsonPath("$.message").value(response.getMessage()),
                            jsonPath("$.errors[0]").value(response.getErrors().get(0)));
        }

        @Test
        void updateShouldReturnExceptionResponse_whenInvalidValueInsadeInRequestBodySend() throws Exception {
            NewsRequestDto newsRequestDto = NewsTestBuilder.builder()
                    .withTitle(null)
                    .build()
                    .buildNewsRequestDto();
            NewsAndNameRequestDto requestDto = new NewsAndNameRequestDto();
            requestDto.setName(AUTHOR_NAME);
            requestDto.setRequestDto(newsRequestDto);
            List<String> errors = List.of("News is not valid");
            ErrorValidationResponse response = new ErrorValidationResponse(
                    HttpStatus.BAD_REQUEST,
                    errors,
                    METHOD_ARGUMENT_NOT_VALID_EXCEPTION_MESSAGE);

            mockMvc.perform(put(url, CORRECT_ID)
                            .contentType(APPLICATION_JSON)
                            .content(mapper.writeValueAsString(requestDto)))
                    .andExpect(status().isBadRequest())
                    .andExpectAll(
                            jsonPath("$.timestamp").isNotEmpty(),
                            jsonPath("$.status").value(response.getStatus()),
                            jsonPath("$.message").value(response.getMessage()),
                            jsonPath("$.errors[0]").value(response.getErrors().get(0)));
        }

        @Test
        void updateShouldReturnExceptionResponse_whenHasNotRightToModify() throws Exception {
            NewsAndNameRequestDto requestDto = NewsTestBuilder.builder()
                    .build()
                    .buildNewsAndNameRequestDto();
            requestDto.setName("Someone else");
            CustomAccessException exception = CustomAccessException.of(News.class);
            ExceptionResponse response = getExceptionResponse(
                    HttpStatus.FORBIDDEN,
                    exception.getMessage(),
                    exception
            );

            mockMvc.perform(put(url, CORRECT_ID)
                            .contentType(APPLICATION_JSON)
                            .content(mapper.writeValueAsString(requestDto)))
                    .andExpect(status().isForbidden())
                    .andExpectAll(
                            jsonPath("$.timestamp").isNotEmpty(),
                            jsonPath("$.status").value(response.getStatus()),
                            jsonPath("$.message").value(response.getMessage()),
                            jsonPath("$.type").value(response.getType()));
        }
    }

    @Nested
    class TestDelete {

        private final String url = "/news/{id}";

        @Test

        //TODO

        @Sql(value = "classpath:sql/insert-before.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
        void deleteShouldReturnSuccessResponse_whenValidId() throws Exception {
            MessageResponse response = getSuccessResponse(DELETION_MESSAGE, News.class);

            mockMvc.perform(delete(url, NEWS_ID)
                            .contentType(APPLICATION_JSON)
                            .content(AUTHOR_NAME))
                    .andExpect(status().isOk())
                    .andExpectAll(
                            jsonPath("$.timestamp").isNotEmpty(),
                            jsonPath("$.status").value(response.getStatus()),
                            jsonPath("$.message").value(response.getMessage()));
        }

        @Test
        void deleteShouldReturnExceptionResponse_whenInvalidId() throws Exception {
            CustomEntityNotFoundException exception = CustomEntityNotFoundException.of(News.class, INCORRECT_ID);
            ExceptionResponse response = getExceptionResponse(
                    HttpStatus.NOT_FOUND,
                    exception.getMessage(),
                    exception
            );

            mockMvc.perform(delete(url, INCORRECT_ID)
                            .contentType(APPLICATION_JSON)
                            .content(AUTHOR_NAME))
                    .andExpect(status().isNotFound())
                    .andExpectAll(
                            jsonPath("$.timestamp").isNotEmpty(),
                            jsonPath("$.status").value(response.getStatus()),
                            jsonPath("$.message").value(response.getMessage()),
                            jsonPath("$.type").value(response.getType()));
        }
    }

    @Nested
    class TestGetNewsSearchResult {

        private final String url = "/news/search/{condition}";

        @Test
        void getAllShouldReturnPageWithNewsResponseDtosList() throws Exception {
            NewsResponseDto news = NewsTestBuilder.builder()
                    .build()
                    .buildNewsResponseDto();

            mockMvc.perform(get(url, news.getTitle()))
                    .andExpect(status().isOk())
                    .andExpectAll(
                            jsonPath("$.content").isArray(),
                            jsonPath("$.content[0].time").value(news.getTime()),
                            jsonPath("$.content[0].title").value(news.getTitle()),
                            jsonPath("$.content[0].text").value(news.getText()),
                            jsonPath("$.content[0].author").value(news.getAuthor()));
        }

        @Test
        void getAllShouldReturnExceptionResponse_whenNewsListIsEmpty() throws Exception {
            CustomNoContentException exception = CustomNoContentException.of(News.class);
            ExceptionResponse response = getExceptionResponse(
                    HttpStatus.GONE,
                    exception.getMessage(),
                    exception
            );

            mockMvc.perform(get(url, "none"))
                    .andExpect(status().isGone())
                    .andExpectAll(
                            jsonPath("$.timestamp").isNotEmpty(),
                            jsonPath("$.status").value(response.getStatus()),
                            jsonPath("$.message").value(response.getMessage()),
                            jsonPath("$.type").value(response.getType()));
        }
    }
}
