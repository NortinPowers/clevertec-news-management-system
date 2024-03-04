package by.clevertec.comment.controller;

import by.clevertec.comment.domain.News;
import by.clevertec.comment.util.NewsTestBuilder;
import by.clevertec.exception.CustomAccessException;
import by.clevertec.exception.CustomEntityNotFoundException;
import by.clevertec.exception.CustomNoContentException;
import by.clevertec.message.MessageResponse;
import by.clevertec.model.ErrorValidationResponse;
import by.clevertec.model.ExceptionResponse;
import by.clevertec.comment.config.TestContainerConfig;
import by.clevertec.comment.domain.Comment;
import by.clevertec.comment.proxy.CommentCacheableAspect;
import by.clevertec.comment.util.CommentTestBuilder;
import by.clevertec.request.CommentAndNamePathRequestDto;
import by.clevertec.request.CommentAndNameRequestDto;
import by.clevertec.request.CommentRequestDto;
import by.clevertec.request.NewsAndNamePathRequestDto;
import by.clevertec.request.NewsAndNameRequestDto;
import by.clevertec.request.NewsRequestDto;
import by.clevertec.response.CommentResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static by.clevertec.comment.util.TestConstant.AUTHOR_NAME;
import static by.clevertec.comment.util.TestConstant.CORRECT_ID;
import static by.clevertec.comment.util.TestConstant.INCORRECT_ID;
import static by.clevertec.comment.util.TestConstant.COMMENT_ID;
import static by.clevertec.comment.util.TestConstant.NEWS_ID;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@Transactional
@AutoConfigureMockMvc
@RequiredArgsConstructor
@SpringBootTest(classes = TestContainerConfig.class)
//TODO
@Sql(value = "classpath:sql/insert-before.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "classpath:sql/reset-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class CommentControllerTest{

    private final ObjectMapper mapper;
    private final MockMvc mockMvc;
    private final CommentCacheableAspect cacheableAspect;

    @Nested
    class TestGetAll {

        private final String url = "/comments";

        @Test
        void getAllShouldReturnPageWithCommentResponseDtosList() throws Exception {
            CommentResponseDto comment = CommentTestBuilder.builder()
                    .build()
                    .buildCommentResponseDto();

            mockMvc.perform(get(url))
                    .andExpect(status().isOk())
                    .andExpectAll(
                            jsonPath("$.content").isArray(),
                            jsonPath("$.content[0].time").value(comment.getTime()),
                            jsonPath("$.content[0].text").value(comment.getText()),
                            jsonPath("$.content[0].username").value(comment.getUsername()),
                            jsonPath("$.content[0].newsId").value(comment.getNewsId()),
                            jsonPath("$.content[0].author").value(comment.getAuthor()));
        }

        @Test

        //TODO

        @Sql(value = "classpath:sql/reset-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
        void getAllShouldReturnExceptionResponse_whenCommentsListIsEmpty() throws Exception {
            CustomNoContentException exception = CustomNoContentException.of(Comment.class);
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
        void getByIdShouldReturnCommentResponseDto() throws Exception {
            CommentResponseDto comment = CommentTestBuilder.builder()
                    .build()
                    .buildCommentResponseDto();
            cacheableAspect.cacheableDelete(COMMENT_ID);
            String url = "/comments/" + COMMENT_ID;

            mockMvc.perform(get(url))
                    .andExpect(status().isOk())
                    .andExpect(content().json(mapper.writeValueAsString(comment)));
        }

        @Test
        void getByIdShouldReturnExceptionResponse_whenIncorrectId() throws Exception {
            CustomEntityNotFoundException exception = CustomEntityNotFoundException.of(Comment.class, INCORRECT_ID);
            ExceptionResponse response = getExceptionResponse(
                    HttpStatus.NOT_FOUND,
                    exception.getMessage(),
                    exception
            );
            String url = "/comments/" + INCORRECT_ID;

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

        private final String url = "/comments";

        @Test
        void saveShouldReturnSuccessResponse_whenValidRequestSend() throws Exception {
            CommentAndNameRequestDto requestDto = CommentTestBuilder.builder()
                    .build()
                    .buildCommentAndNameRequestDto();
            MessageResponse response = getSuccessResponse(CREATION_MESSAGE, Comment.class);

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

        private final String url = "/comments/{id}";
        private final CommentAndNamePathRequestDto correctRequestDto = CommentTestBuilder.builder()
                .withText("new Text")
                .build()
                .buildCommentAndNamePathRequestDto();

        @Test
        void updateShouldReturnSuccessResponse_whenValidRequestSend() throws Exception {
            MessageResponse response = getSuccessResponse(UPDATE_MESSAGE, Comment.class);

            mockMvc.perform(post(url, COMMENT_ID)
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
            CustomEntityNotFoundException exception = CustomEntityNotFoundException.of(Comment.class, INCORRECT_ID);
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

            mockMvc.perform(post(url, COMMENT_ID)
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
            CommentAndNamePathRequestDto requestDto = CommentTestBuilder.builder()
                    .build()
                    .buildCommentAndNamePathRequestDto();
            requestDto.setName("Someone else");
            CustomAccessException exception = CustomAccessException.of(Comment.class);
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
    class TestPutUpdate {

        private final String url = "/comments/{id}";
        private final CommentAndNameRequestDto correctRequestDto = CommentTestBuilder.builder()
                .withText("new text")
                .build()
                .buildCommentAndNameRequestDto();

        @Test
        void updateShouldReturnSuccessResponse_whenValidRequestSend() throws Exception {
            MessageResponse response = getSuccessResponse(UPDATE_MESSAGE, Comment.class);

            mockMvc.perform(put(url, COMMENT_ID)
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
            CustomEntityNotFoundException exception = CustomEntityNotFoundException.of(Comment.class, INCORRECT_ID);
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

            mockMvc.perform(put(url, COMMENT_ID)
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
            CommentRequestDto commentRequestDto = CommentTestBuilder.builder()
                    .build()
                    .buildCommentRequestDto();
            CommentAndNameRequestDto requestDto = new CommentAndNameRequestDto();
            requestDto.setRequestDto(commentRequestDto);
            List<String> errors = List.of("Enter name");
            ErrorValidationResponse response = new ErrorValidationResponse(
                    HttpStatus.BAD_REQUEST,
                    errors,
                    METHOD_ARGUMENT_NOT_VALID_EXCEPTION_MESSAGE);

            mockMvc.perform(put(url, COMMENT_ID)
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
            CommentRequestDto commentRequestDto = CommentTestBuilder.builder()
                    .withText(null)
                    .build()
                    .buildCommentRequestDto();
            CommentAndNameRequestDto requestDto = new CommentAndNameRequestDto();
            requestDto.setName(AUTHOR_NAME);
            requestDto.setRequestDto(commentRequestDto);
            List<String> errors = List.of("Comment is not valid");
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
            CommentAndNameRequestDto requestDto = CommentTestBuilder.builder()
                    .build()
                    .buildCommentAndNameRequestDto();
            requestDto.setName("Someone else");
            CustomAccessException exception = CustomAccessException.of(Comment.class);
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

        private final String url = "/comments/{id}";

        @Test

        //TODO

        @Sql(value = "classpath:sql/insert-before.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
        void deleteShouldReturnSuccessResponse_whenValidId() throws Exception {
            MessageResponse response = getSuccessResponse(DELETION_MESSAGE, Comment.class);

            mockMvc.perform(delete(url, COMMENT_ID)
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
            CustomEntityNotFoundException exception = CustomEntityNotFoundException.of(Comment.class, INCORRECT_ID);
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
    class TestGetCommentSearchResult {

        private final String url = "/comments/search/{condition}";

        @Test
        void getAllShouldReturnPageWithCommentResponseDtosList() throws Exception {
            CommentResponseDto comment = CommentTestBuilder.builder()
                    .build()
                    .buildCommentResponseDto();

            mockMvc.perform(get(url, comment.getAuthor()))
                    .andExpect(status().isOk())
                    .andExpectAll(
                            jsonPath("$.content").isArray(),
                            jsonPath("$.content[0].time").value(comment.getTime()),
                            jsonPath("$.content[0].text").value(comment.getText()),
                            jsonPath("$.content[0].username").value(comment.getUsername()),
                            jsonPath("$.content[0].newsId").value(comment.getNewsId()),
                            jsonPath("$.content[0].author").value(comment.getAuthor()));
        }

        @Test
        void getAllShouldReturnExceptionResponse_whenCommentsListIsEmpty() throws Exception {
            CustomNoContentException exception = CustomNoContentException.of(Comment.class);
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

    @Nested
    class TestGetAllByNewsId {

        private final String url = "/comments/news/{newsId}";

        @Test
        void getAllByNewsIdShouldReturnPageWithCommentResponseDtosList() throws Exception {
            CommentResponseDto comment = CommentTestBuilder.builder()
                    .build()
                    .buildCommentResponseDto();

            mockMvc.perform(get(url, NEWS_ID))
                    .andExpect(status().isOk())
                    .andExpectAll(
                            jsonPath("$.content").isArray(),
                            jsonPath("$.content[0].time").value(comment.getTime()),
                            jsonPath("$.content[0].text").value(comment.getText()),
                            jsonPath("$.content[0].username").value(comment.getUsername()),
                            jsonPath("$.content[0].newsId").value(comment.getNewsId()),
                            jsonPath("$.content[0].author").value(comment.getAuthor()));
        }

        @Test

        //TODO

        @Sql(value = "classpath:sql/reset-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
        void getAllByNewsIdShouldReturnExceptionResponse_whenCommentsListIsEmpty() throws Exception {
            CustomNoContentException exception = CustomNoContentException.of(Comment.class);
            ExceptionResponse response = getExceptionResponse(
                    HttpStatus.GONE,
                    exception.getMessage(),
                    exception
            );

            mockMvc.perform(get(url, NEWS_ID))
                    .andExpect(status().isGone())
                    .andExpectAll(
                            jsonPath("$.timestamp").isNotEmpty(),
                            jsonPath("$.status").value(response.getStatus()),
                            jsonPath("$.message").value(response.getMessage()),
                            jsonPath("$.type").value(response.getType()));
        }
    }
}
