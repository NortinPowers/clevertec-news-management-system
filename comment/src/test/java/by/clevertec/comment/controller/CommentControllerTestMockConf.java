package by.clevertec.comment.controller;

import static by.clevertec.comment.util.TestConstant.CORRECT_ID;
import static by.clevertec.comment.util.TestConstant.PAGE_NUMBER;
import static by.clevertec.comment.util.TestConstant.PAGE_SIZE;
import static by.clevertec.util.ResponseUtils.getExceptionResponse;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import by.clevertec.comment.domain.Comment;
import by.clevertec.comment.service.CommentService;
import by.clevertec.comment.util.CommentTestBuilder;
import by.clevertec.exception.CustomNoContentException;
import by.clevertec.model.ExceptionResponse;
import by.clevertec.response.CommentResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@RequiredArgsConstructor
class CommentControllerTestMockConf {

    private final ObjectMapper mapper;
    private final MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    @Test
    void getByIdShouldReturnCommentResponseDto() throws Exception {
        CommentResponseDto comment = CommentTestBuilder.builder()
                .build()
                .buildCommentResponseDto();
        String url = "/comments/" + CORRECT_ID;

        when(commentService.getById(CORRECT_ID))
                .thenReturn(comment);

        mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(comment)));
    }

    @Nested
    class TestGetAll {

        private final String url = "/comments";

        @Test
        void getAllShouldReturnPageWithCommentResponseDtosList() throws Exception {
            CommentResponseDto comment = CommentTestBuilder.builder()
                    .build()
                    .buildCommentResponseDto();
            List<CommentResponseDto> commentList = List.of(comment);
            PageImpl<CommentResponseDto> page = new PageImpl<>(commentList);
            PageRequest pageRequest = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);

            when(commentService.getAll(pageRequest))
                    .thenReturn(page);

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
        void getAllShouldReturnExceptionResponse_whenCommentListIsEmpty() throws Exception {
            PageRequest pageRequest = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);
            CustomNoContentException exception = CustomNoContentException.of(Comment.class);
            ExceptionResponse response = getExceptionResponse(
                    HttpStatus.GONE,
                    exception.getMessage(),
                    exception
            );

            when(commentService.getAll(pageRequest))
                    .thenThrow(exception);

            mockMvc.perform(get(url))
                    .andExpect(status().isGone())
                    .andExpectAll(
                            jsonPath("$.status").value(response.getStatus()),
                            jsonPath("$.message").value(response.getMessage()),
                            jsonPath("$.type").value(response.getType()));
        }
    }
}
