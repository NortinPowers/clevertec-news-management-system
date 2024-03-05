package by.clevertec.news.service.impl;

import static by.clevertec.news.util.TestConstant.AUTHOR_NAME;
import static by.clevertec.news.util.TestConstant.CORRECT_ID;
import static by.clevertec.news.util.TestConstant.INCORRECT_ID;
import static by.clevertec.news.util.TestConstant.PAGE_NUMBER;
import static by.clevertec.news.util.TestConstant.PAGE_SIZE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import by.clevertec.exception.CustomAccessException;
import by.clevertec.exception.CustomEntityNotFoundException;
import by.clevertec.exception.CustomNoContentException;
import by.clevertec.news.domain.Author;
import by.clevertec.news.domain.News;
import by.clevertec.news.mapper.NewsMapper;
import by.clevertec.news.repository.NewsRepository;
import by.clevertec.news.service.AuthorService;
import by.clevertec.news.util.NewsTestBuilder;
import by.clevertec.request.NewsAndNamePathRequestDto;
import by.clevertec.request.NewsAndNameRequestDto;
import by.clevertec.request.NewsPathRequestDto;
import by.clevertec.request.NewsRequestDto;
import by.clevertec.response.NewsResponseDto;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
class NewsServiceImplTest {

    @Mock
    private NewsMapper newsMapper;

    @Mock
    private NewsRepository newsRepository;

    @Mock
    private AuthorService authorService;

    @InjectMocks
    private NewsServiceImpl newsService;

    @Captor
    private ArgumentCaptor<News> captor;

    @Nested
    class GetAllTest {

        private final PageRequest pageRequest = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);

        @Test
        void getAllShouldReturnNewsResponseDtosList_whenNewsListIsNotEmpty() {
            NewsResponseDto newsResponseDto = NewsTestBuilder.builder()
                    .build()
                    .buildNewsResponseDto();
            List<NewsResponseDto> expected = List.of(newsResponseDto);
            News news = NewsTestBuilder.builder()
                    .build()
                    .buildNews();
            List<News> newsList = List.of(news);
            PageImpl<News> page = new PageImpl<>(newsList);

            when(newsRepository.findAll(pageRequest))
                    .thenReturn(page);
            when(newsMapper.toDto(news))
                    .thenReturn(newsResponseDto);

            Page<NewsResponseDto> actual = newsService.getAll(pageRequest);

            assertThat(actual.getContent())
                    .hasSize(expected.size())
                    .isEqualTo(expected);
        }

        @Test
        void getAllShouldThrowCustomNoContentException_whenNewsListIsEmpty() {
            CustomNoContentException expectedException = CustomNoContentException.of(News.class);
            List<News> newsList = List.of();
            PageImpl<News> page = new PageImpl<>(newsList);

            when(newsRepository.findAll(pageRequest))
                    .thenReturn(page);

            CustomNoContentException actualException = assertThrows(CustomNoContentException.class, () -> newsService.getAll(pageRequest));

            assertEquals(expectedException.getMessage(), actualException.getMessage());
            verify(newsMapper, never()).toDto(any(News.class));
        }
    }

    @Nested
    class GetByIdTest {

        @Test
        void getShouldReturnNewsResponseDto_whenCorrectId() {
            NewsResponseDto expected = NewsTestBuilder.builder()
                    .build()
                    .buildNewsResponseDto();
            News news = NewsTestBuilder.builder()
                    .build()
                    .buildNews();
            Optional<News> optionalNews = Optional.of(news);
            Long id = news.getId();

            when(newsRepository.findById(id))
                    .thenReturn(optionalNews);
            when(newsMapper.toDto(news))
                    .thenReturn(expected);

            NewsResponseDto actual = newsService.getById(id);

            assertThat(actual)
                    .hasFieldOrPropertyWithValue(News.Fields.time, expected.getTime())
                    .hasFieldOrPropertyWithValue(News.Fields.title, expected.getTitle())
                    .hasFieldOrPropertyWithValue(News.Fields.text, expected.getText())
                    .hasFieldOrPropertyWithValue("author", expected.getAuthor());
        }

        @Test
        void getShouldThrowCustomEntityNotFoundException_whenIncorectId() {
            CustomEntityNotFoundException expectedException = CustomEntityNotFoundException.of(News.class, INCORRECT_ID);

            when(newsRepository.findById(INCORRECT_ID))
                    .thenReturn(Optional.empty());

            CustomEntityNotFoundException actualException = assertThrows(CustomEntityNotFoundException.class, () -> newsService.getById(INCORRECT_ID));

            verify(newsMapper, never()).toDto(any(News.class));
            assertEquals(expectedException.getMessage(), actualException.getMessage());
        }
    }

    @Nested
    class SaveTest {

        @Test
        void saveShouldSaveHouses_whenNewsAndNameRequestDtoTransferredWithCorrectParameter() {
            News news = NewsTestBuilder.builder()
                    .withId(null)
                    .build()
                    .buildNews();
            News saved = NewsTestBuilder.builder()
                    .withId(null)
                    .build()
                    .buildNews();
            NewsAndNameRequestDto requestDto = NewsTestBuilder.builder()
                    .build()
                    .buildNewsAndNameRequestDto();
            NewsRequestDto newsRequestDto = requestDto.getRequestDto();
            Author author = new Author(AUTHOR_NAME);

            when(newsMapper.toDomain(newsRequestDto))
                    .thenReturn(news);
            when(authorService.getByName(AUTHOR_NAME))
                    .thenReturn(author);
            when(newsRepository.save(news))
                    .thenReturn(saved);

            newsService.save(requestDto);
        }

        @Test
        void createShouldSetNewsId_whenNewsSaved() {
            News news = NewsTestBuilder.builder()
                    .withId(null)
                    .build()
                    .buildNews();
            News saved = NewsTestBuilder.builder()
                    .withId(null)
                    .build()
                    .buildNews();
            NewsAndNameRequestDto requestDto = NewsTestBuilder.builder()
                    .build()
                    .buildNewsAndNameRequestDto();
            NewsRequestDto newsRequestDto = requestDto.getRequestDto();
            Author author = new Author(AUTHOR_NAME);

            when(newsMapper.toDomain(newsRequestDto))
                    .thenReturn(news);
            when(authorService.getByName(AUTHOR_NAME))
                    .thenReturn(author);
            when(newsRepository.save(news))
                    .thenReturn(saved);

            newsService.save(requestDto);

            verify(newsRepository).save(captor.capture());
            assertNotNull(captor.getValue());
        }
    }

    @Nested
    class UpdateTest {

        @Test
        void updateShouldThrowCustomEntityNotFoundException_whenIncorrectId() {
            when(newsRepository.findById(INCORRECT_ID))
                    .thenReturn(Optional.empty());

            assertThrows(CustomEntityNotFoundException.class, () -> newsService.update(INCORRECT_ID, any(NewsAndNameRequestDto.class)));
            verify(newsMapper, never()).toDomain(any(NewsRequestDto.class));
            verify(newsMapper, never()).merge(any(News.class), any(News.class));
        }

        @Test
        void updateShouldThrowCustomAccessException_whenHasNotRightToModify() {
            News news = NewsTestBuilder.builder()
                    .withAuthor(new Author("Someone Else"))
                    .build()
                    .buildNews();
            NewsAndNameRequestDto newsAndNameRequestDto = NewsTestBuilder.builder()
                    .build()
                    .buildNewsAndNameRequestDto();

            when(newsRepository.findById(CORRECT_ID))
                    .thenReturn(Optional.of(news));

            assertThrows(CustomAccessException.class, () -> newsService.update(CORRECT_ID, newsAndNameRequestDto));
            verify(newsMapper, never()).toDomain(any(NewsRequestDto.class));
            verify(newsMapper, never()).merge(any(News.class), any(News.class));
        }

        @Test
        void updateShouldUpdateNews_whenCorrectIdAndNameAndDtoTransferred() {
            String text = "new news text";
            News news = NewsTestBuilder.builder()
                    .build()
                    .buildNews();
            Optional<News> optionalNews = Optional.of(news);
            NewsAndNameRequestDto requestDto = NewsTestBuilder.builder()
                    .withText(text)
                    .build()
                    .buildNewsAndNameRequestDto();
            NewsRequestDto newsRequestDto = requestDto.getRequestDto();
            News updated = NewsTestBuilder.builder()
                    .withText(text)
                    .build()
                    .buildNews();
            News merged = NewsTestBuilder.builder()
                    .withText(text)
                    .build()
                    .buildNews();

            when(newsRepository.findById(CORRECT_ID))
                    .thenReturn(optionalNews);
            when(newsMapper.toDomain(newsRequestDto))
                    .thenReturn(updated);
            when(newsMapper.merge(news, updated))
                    .thenReturn(merged);

            newsService.update(CORRECT_ID, requestDto);
        }

        @Test
        void updateShouldUpdateNews_whenCorrectIdAndDtoTransferredAndUserIsAdmin() {
            String text = "new news text";
            News news = NewsTestBuilder.builder()
                    .build()
                    .buildNews();
            Optional<News> optionalNews = Optional.of(news);
            NewsAndNameRequestDto requestDto = NewsTestBuilder.builder()
                    .withText(text)
                    .build()
                    .buildNewsAndNameRequestDto();
            requestDto.setName("!ADMIN");
            NewsRequestDto newsRequestDto = requestDto.getRequestDto();
            News updated = NewsTestBuilder.builder()
                    .withText(text)
                    .build()
                    .buildNews();
            News merged = NewsTestBuilder.builder()
                    .withText(text)
                    .build()
                    .buildNews();

            when(newsRepository.findById(CORRECT_ID))
                    .thenReturn(optionalNews);
            when(newsMapper.toDomain(newsRequestDto))
                    .thenReturn(updated);
            when(newsMapper.merge(news, updated))
                    .thenReturn(merged);

            newsService.update(CORRECT_ID, requestDto);
        }
    }

    @Nested
    class UpdatePathTest {

        @Test
        void updateShouldThrowCustomEntityNotFoundException_whenIncorrectId() {
            when(newsRepository.findById(INCORRECT_ID))
                    .thenReturn(Optional.empty());

            assertThrows(CustomEntityNotFoundException.class, () -> newsService.updatePath(INCORRECT_ID, any(NewsAndNamePathRequestDto.class)));
            verify(newsMapper, never()).toDomain(any(NewsRequestDto.class));
            verify(newsMapper, never()).merge(any(News.class), any(News.class));
        }

        @Test
        void updateShouldThrowCustomAccessException_whenHasNotRightToModify() {
            News news = NewsTestBuilder.builder()
                    .withAuthor(new Author("Someone Else"))
                    .build()
                    .buildNews();
            NewsAndNamePathRequestDto requestDto = NewsTestBuilder.builder()
                    .build()
                    .buildNewsAndNamePathRequestDto();

            when(newsRepository.findById(CORRECT_ID))
                    .thenReturn(Optional.of(news));

            assertThrows(CustomAccessException.class, () -> newsService.updatePath(CORRECT_ID, requestDto));
            verify(newsMapper, never()).toDomain(any(NewsRequestDto.class));
            verify(newsMapper, never()).merge(any(News.class), any(News.class));
        }

        @Test
        void updateShouldUpdateNews_whenCorrectIdAndNameAndDtoTransferred() {
            String text = "new news text";
            News news = NewsTestBuilder.builder()
                    .build()
                    .buildNews();
            Optional<News> optionalNews = Optional.of(news);
            NewsAndNamePathRequestDto requestDto = NewsTestBuilder.builder()
                    .withText(text)
                    .withTime(null)
                    .build()
                    .buildNewsAndNamePathRequestDto();
            NewsPathRequestDto newsPathRequestDto = requestDto.getRequestDto();
            News updated = NewsTestBuilder.builder()
                    .withText(text)
                    .withTime(null)
                    .build()
                    .buildNews();
            News merged = NewsTestBuilder.builder()
                    .withText(text)
                    .build()
                    .buildNews();

            when(newsRepository.findById(CORRECT_ID))
                    .thenReturn(optionalNews);
            when(newsMapper.toDomain(newsPathRequestDto))
                    .thenReturn(updated);
            when(newsMapper.merge(news, updated))
                    .thenReturn(merged);

            newsService.updatePath(CORRECT_ID, requestDto);
        }

        @Test
        void updateShouldUpdateNews_whenCorrectIdAndDtoTransferredAndUserIsAdmin() {
            String text = "new news text";
            News news = NewsTestBuilder.builder()
                    .build()
                    .buildNews();
            Optional<News> optionalNews = Optional.of(news);
            NewsAndNamePathRequestDto requestDto = NewsTestBuilder.builder()
                    .withText(text)
                    .withTime(null)
                    .build()
                    .buildNewsAndNamePathRequestDto();
            requestDto.setName("!ADMIN");
            NewsPathRequestDto newsPathRequestDto = requestDto.getRequestDto();
            News updated = NewsTestBuilder.builder()
                    .withText(text)
                    .withTime(null)
                    .build()
                    .buildNews();
            News merged = NewsTestBuilder.builder()
                    .withText(text)
                    .build()
                    .buildNews();

            when(newsRepository.findById(CORRECT_ID))
                    .thenReturn(optionalNews);
            when(newsMapper.toDomain(newsPathRequestDto))
                    .thenReturn(updated);
            when(newsMapper.merge(news, updated))
                    .thenReturn(merged);

            newsService.updatePath(CORRECT_ID, requestDto);
        }
    }

    @Nested
    class DeleteTest {

        @Test
        void deleteShouldDeleteNews_whenCorrectIdAndUsername() {
            News news = NewsTestBuilder.builder()
                    .build()
                    .buildNews();

            when(newsRepository.findById(CORRECT_ID))
                    .thenReturn(Optional.of(news));

            newsService.delete(CORRECT_ID, AUTHOR_NAME);
        }

        @Test
        void deleteShouldDeleteNews_whenCorrectIdAndUserIsAdmin() {
            News news = NewsTestBuilder.builder()
                    .build()
                    .buildNews();
            String admin = "!ADMIN";

            when(newsRepository.findById(CORRECT_ID))
                    .thenReturn(Optional.of(news));

            newsService.delete(CORRECT_ID, admin);
        }

        @Test
        void deleteShouldThrowCustomEntityNotFoundException_whenIncorrectId() {
            CustomEntityNotFoundException expectedException = CustomEntityNotFoundException.of(News.class, INCORRECT_ID);

            when(newsRepository.findById(INCORRECT_ID))
                    .thenReturn(Optional.empty());

            CustomEntityNotFoundException actualException = assertThrows(CustomEntityNotFoundException.class, () -> newsService.delete(INCORRECT_ID, AUTHOR_NAME));

            assertEquals(expectedException.getMessage(), actualException.getMessage());
            verify(newsRepository, never()).delete(any(News.class));
        }

        @Test
        void deleteShouldCustomAccessException_whenHasNotRightToModify() {
            News news = NewsTestBuilder.builder()
                    .build()
                    .buildNews();
            String name = "Someone Else";
            CustomAccessException expectedException = CustomAccessException.of(News.class);

            when(newsRepository.findById(CORRECT_ID))
                    .thenReturn(Optional.of(news));

            CustomAccessException actualException = assertThrows(CustomAccessException.class, () -> newsService.delete(CORRECT_ID, name));

            assertEquals(expectedException.getMessage(), actualException.getMessage());
            verify(newsRepository, never()).delete(any(News.class));
        }
    }

    @Nested
    class TestFindNewsSearchResult {

        private final PageRequest pageRequest = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);

        @Test
        void getNewsSearchResultShouldReturnPageWithNewsResponseDto_whenValidConditionPassed() {
            NewsResponseDto newsResponseDto = NewsTestBuilder.builder()
                    .build()
                    .buildNewsResponseDto();
            List<NewsResponseDto> expected = List.of(newsResponseDto);
            News news = NewsTestBuilder.builder()
                    .build()
                    .buildNews();
            List<News> newsList = List.of(news);
            PageImpl<News> page = new PageImpl<>(newsList);
            String condition = "valid condition";

            when(newsRepository.getNewsSearchResult(condition, pageRequest))
                    .thenReturn(page);
            when(newsMapper.toDto(news))
                    .thenReturn(newsResponseDto);

            Page<NewsResponseDto> actual = newsService.findNewsSearchResult(condition, pageRequest);

            assertThat(actual.getContent())
                    .hasSize(expected.size())
                    .isEqualTo(expected);
        }

        @Test
        void getNewsSearchResultShouldThrowCustomNoContentException_whenFoundedNewssListIsEmpty() {
            CustomNoContentException expectedException = CustomNoContentException.of(News.class);
            List<News> houses = List.of();
            PageImpl<News> page = new PageImpl<>(houses);
            String condition = "invalid condition";

            when(newsRepository.getNewsSearchResult(condition, pageRequest))
                    .thenReturn(page);

            CustomNoContentException actualException = assertThrows(CustomNoContentException.class, () -> newsService.findNewsSearchResult(condition, pageRequest));

            assertEquals(expectedException.getMessage(), actualException.getMessage());
            verify(newsMapper, never()).toDto(any(News.class));
        }
    }
}
