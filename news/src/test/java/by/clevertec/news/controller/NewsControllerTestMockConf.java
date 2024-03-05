package by.clevertec.news.controller;

import static by.clevertec.news.util.TestConstant.CORRECT_ID;
import static by.clevertec.news.util.TestConstant.PAGE_NUMBER;
import static by.clevertec.news.util.TestConstant.PAGE_SIZE;
import static by.clevertec.util.ResponseUtils.getExceptionResponse;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import by.clevertec.exception.CustomNoContentException;
import by.clevertec.model.ExceptionResponse;
import by.clevertec.news.domain.News;
import by.clevertec.news.service.NewsService;
import by.clevertec.news.util.NewsTestBuilder;
import by.clevertec.response.NewsResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@RequiredArgsConstructor
class NewsControllerTestMockConf {

    private final ObjectMapper mapper;
    private final MockMvc mockMvc;

    @MockBean
    private NewsService newsService;

    @Test
    void getByIdShouldReturnNewsResponseDto() throws Exception {
        NewsResponseDto news = NewsTestBuilder.builder()
                .build()
                .buildNewsResponseDto();
        String url = "/news/" + CORRECT_ID;

        when(newsService.getById(CORRECT_ID))
                .thenReturn(news);

        mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(news)));
    }

    @Test
    void getAllShouldReturnExceptionResponse_whenNewsListIsEmpty() throws Exception {
        PageRequest pageRequest = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);
        CustomNoContentException exception = CustomNoContentException.of(News.class);
        ExceptionResponse response = getExceptionResponse(
                HttpStatus.GONE,
                exception.getMessage(),
                exception
        );

        when(newsService.getAll(pageRequest))
                .thenThrow(exception);

        mockMvc.perform(get("/news"))
                .andExpect(status().isGone())
                .andExpectAll(
                        jsonPath("$.status").value(response.getStatus()),
                        jsonPath("$.message").value(response.getMessage()),
                        jsonPath("$.type").value(response.getType()));
    }

}
