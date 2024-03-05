package by.clevertec.comment.service.impl;

import static by.clevertec.comment.util.TestConstant.AUTHOR_NAME;
import static by.clevertec.comment.util.TestConstant.CORRECT_ID;
import static by.clevertec.comment.util.TestConstant.INCORRECT_ID;
import static by.clevertec.comment.util.TestConstant.NEWS_ID;
import static by.clevertec.comment.util.TestConstant.PAGE_NUMBER;
import static by.clevertec.comment.util.TestConstant.PAGE_SIZE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import by.clevertec.comment.domain.Author;
import by.clevertec.comment.domain.Comment;
import by.clevertec.comment.mapper.CommentMapper;
import by.clevertec.comment.repository.CommentRepository;
import by.clevertec.comment.service.AuthorService;
import by.clevertec.comment.util.CommentTestBuilder;
import by.clevertec.exception.CustomAccessException;
import by.clevertec.exception.CustomEntityNotFoundException;
import by.clevertec.exception.CustomNoContentException;
import by.clevertec.request.CommentAndNamePathRequestDto;
import by.clevertec.request.CommentAndNameRequestDto;
import by.clevertec.request.CommentPathRequestDto;
import by.clevertec.request.CommentRequestDto;
import by.clevertec.response.CommentResponseDto;
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
class CommentServiceImplTest {

    @Mock
    private CommentMapper commentMapper;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private AuthorService authorService;

    @InjectMocks
    private CommentServiceImpl commentService;

    @Captor
    private ArgumentCaptor<Comment> captor;

    @Nested
    class GetAllTest {

        private final PageRequest pageRequest = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);

        @Test
        void getAllShouldReturnCommentResponseDtosList_whenCommentListIsNotEmpty() {
            CommentResponseDto commentResponseDto = CommentTestBuilder.builder()
                    .build()
                    .buildCommentResponseDto();
            List<CommentResponseDto> expected = List.of(commentResponseDto);
            Comment comment = CommentTestBuilder.builder()
                    .build()
                    .buildComment();
            List<Comment> commentList = List.of(comment);
            PageImpl<Comment> page = new PageImpl<>(commentList);

            when(commentRepository.findAll(pageRequest))
                    .thenReturn(page);
            when(commentMapper.toDto(comment))
                    .thenReturn(commentResponseDto);

            Page<CommentResponseDto> actual = commentService.getAll(pageRequest);

            assertThat(actual.getContent())
                    .hasSize(expected.size())
                    .isEqualTo(expected);
        }

        @Test
        void getAllShouldThrowCustomNoContentException_whenCommentListIsEmpty() {
            CustomNoContentException expectedException = CustomNoContentException.of(Comment.class);
            List<Comment> commentList = List.of();
            PageImpl<Comment> page = new PageImpl<>(commentList);

            when(commentRepository.findAll(pageRequest))
                    .thenReturn(page);

            CustomNoContentException actualException = assertThrows(CustomNoContentException.class, () -> commentService.getAll(pageRequest));

            assertEquals(expectedException.getMessage(), actualException.getMessage());
            verify(commentMapper, never()).toDto(any(Comment.class));
        }
    }

    @Nested
    class GetByIdTest {

        @Test
        void getShouldReturnCommentResponseDto_whenCorrectId() {
            CommentResponseDto expected = CommentTestBuilder.builder()
                    .build()
                    .buildCommentResponseDto();
            Comment comment = CommentTestBuilder.builder()
                    .build()
                    .buildComment();
            Optional<Comment> optionalComment = Optional.of(comment);
            Long id = comment.getId();

            when(commentRepository.findById(id))
                    .thenReturn(optionalComment);
            when(commentMapper.toDto(comment))
                    .thenReturn(expected);

            CommentResponseDto actual = commentService.getById(id);

            assertThat(actual)
                    .hasFieldOrPropertyWithValue(Comment.Fields.time, expected.getTime())
                    .hasFieldOrPropertyWithValue(Comment.Fields.text, expected.getText())
                    .hasFieldOrPropertyWithValue(Comment.Fields.username, expected.getUsername())
                    .hasFieldOrPropertyWithValue("newsId", expected.getNewsId())
                    .hasFieldOrPropertyWithValue("author", expected.getAuthor());
        }

        @Test
        void getShouldThrowCustomEntityNotFoundException_whenIncorectId() {
            CustomEntityNotFoundException expectedException = CustomEntityNotFoundException.of(Comment.class, INCORRECT_ID);

            when(commentRepository.findById(INCORRECT_ID))
                    .thenReturn(Optional.empty());

            CustomEntityNotFoundException actualException = assertThrows(CustomEntityNotFoundException.class, () -> commentService.getById(INCORRECT_ID));

            verify(commentMapper, never()).toDto(any(Comment.class));
            assertEquals(expectedException.getMessage(), actualException.getMessage());
        }
    }

    @Nested
    class SaveTest {

        @Test
        void saveShouldSaveComment_whenCommentAndNameRequestDtoTransferredWithCorrectParameter() {
            Comment comment = CommentTestBuilder.builder()
                    .withId(null)
                    .build()
                    .buildComment();
            Comment saved = CommentTestBuilder.builder()
                    .build()
                    .buildComment();
            CommentAndNameRequestDto requestDto = CommentTestBuilder.builder()
                    .build()
                    .buildCommentAndNameRequestDto();
            CommentRequestDto commentRequestDto = requestDto.getRequestDto();
            Author author = new Author(AUTHOR_NAME);

            when(commentMapper.toDomain(commentRequestDto))
                    .thenReturn(comment);
            when(authorService.getByName(AUTHOR_NAME))
                    .thenReturn(author);
            when(commentRepository.save(comment))
                    .thenReturn(saved);

            commentService.save(requestDto);
        }

        @Test
        void createShouldSetCommentId_whenCommentSaved() {
            Comment comment = CommentTestBuilder.builder()
                    .withId(null)
                    .build()
                    .buildComment();
            Comment saved = CommentTestBuilder.builder()
                    .build()
                    .buildComment();
            CommentAndNameRequestDto requestDto = CommentTestBuilder.builder()
                    .build()
                    .buildCommentAndNameRequestDto();
            CommentRequestDto commentRequestDto = requestDto.getRequestDto();
            Author author = new Author(AUTHOR_NAME);

            when(commentMapper.toDomain(commentRequestDto))
                    .thenReturn(comment);
            when(authorService.getByName(AUTHOR_NAME))
                    .thenReturn(author);
            when(commentRepository.save(comment))
                    .thenReturn(saved);

            commentService.save(requestDto);

            verify(commentRepository).save(captor.capture());
            assertNotNull(captor.getValue());
        }
    }

    @Nested
    class UpdateTest {

        @Test
        void updateShouldThrowCustomEntityNotFoundException_whenIncorrectId() {
            when(commentRepository.findById(INCORRECT_ID))
                    .thenReturn(Optional.empty());

            assertThrows(CustomEntityNotFoundException.class, () -> commentService.update(INCORRECT_ID, any(CommentAndNameRequestDto.class)));
            verify(commentMapper, never()).toDomain(any(CommentRequestDto.class));
            verify(commentMapper, never()).merge(any(Comment.class), any(Comment.class));
        }

        @Test
        void updateShouldThrowCustomAccessException_whenHasNotRightToModify() {
            Comment comment = CommentTestBuilder.builder()
                    .withAuthor(new Author("Someone Else"))
                    .build()
                    .buildComment();
            CommentAndNameRequestDto commentAndNameRequestDto = CommentTestBuilder.builder()
                    .build()
                    .buildCommentAndNameRequestDto();

            when(commentRepository.findById(CORRECT_ID))
                    .thenReturn(Optional.of(comment));

            assertThrows(CustomAccessException.class, () -> commentService.update(CORRECT_ID, commentAndNameRequestDto));
            verify(commentMapper, never()).toDomain(any(CommentRequestDto.class));
            verify(commentMapper, never()).merge(any(Comment.class), any(Comment.class));
        }

        @Test
        void updateShouldUpdateComment_whenCorrectIdAndNameAndDtoTransferred() {
            String text = "new comment text";
            Comment comment = CommentTestBuilder.builder()
                    .build()
                    .buildComment();
            Optional<Comment> optionalComment = Optional.of(comment);
            CommentAndNameRequestDto requestDto = CommentTestBuilder.builder()
                    .withText(text)
                    .build()
                    .buildCommentAndNameRequestDto();
            CommentRequestDto commentRequestDto = requestDto.getRequestDto();
            Comment updated = CommentTestBuilder.builder()
                    .withText(text)
                    .build()
                    .buildComment();
            Comment merged = CommentTestBuilder.builder()
                    .withText(text)
                    .build()
                    .buildComment();

            when(commentRepository.findById(CORRECT_ID))
                    .thenReturn(optionalComment);
            when(commentMapper.toDomain(commentRequestDto))
                    .thenReturn(updated);
            when(commentMapper.merge(comment, updated))
                    .thenReturn(merged);

            commentService.update(CORRECT_ID, requestDto);
        }

        @Test
        void updateShouldUpdateComment_whenCorrectIdAndDtoTransferredAndUserIsAdmin() {
            String text = "new comment text";
            Comment comment = CommentTestBuilder.builder()
                    .build()
                    .buildComment();
            Optional<Comment> optionalComment = Optional.of(comment);
            CommentAndNameRequestDto requestDto = CommentTestBuilder.builder()
                    .withText(text)
                    .build()
                    .buildCommentAndNameRequestDto();
            requestDto.setName("!ADMIN");
            CommentRequestDto commentRequestDto = requestDto.getRequestDto();
            Comment updated = CommentTestBuilder.builder()
                    .withText(text)
                    .build()
                    .buildComment();
            Comment merged = CommentTestBuilder.builder()
                    .withText(text)
                    .build()
                    .buildComment();

            when(commentRepository.findById(CORRECT_ID))
                    .thenReturn(optionalComment);
            when(commentMapper.toDomain(commentRequestDto))
                    .thenReturn(updated);
            when(commentMapper.merge(comment, updated))
                    .thenReturn(merged);

            commentService.update(CORRECT_ID, requestDto);
        }
    }

    @Nested
    class UpdatePathTest {


        @Test
        void updateShouldThrowCustomEntityNotFoundException_whenIncorrectId() {
            when(commentRepository.findById(INCORRECT_ID))
                    .thenReturn(Optional.empty());

            assertThrows(CustomEntityNotFoundException.class, () -> commentService.updatePath(INCORRECT_ID, any(CommentAndNamePathRequestDto.class)));
            verify(commentMapper, never()).toDomain(any(CommentRequestDto.class));
            verify(commentMapper, never()).merge(any(Comment.class), any(Comment.class));
        }

        @Test
        void updateShouldThrowCustomAccessException_whenHasNotRightToModify() {
            Comment comment = CommentTestBuilder.builder()
                    .withAuthor(new Author("Someone Else"))
                    .build()
                    .buildComment();
            CommentAndNamePathRequestDto requestDto = CommentTestBuilder.builder()
                    .build()
                    .buildCommentAndNamePathRequestDto();

            when(commentRepository.findById(CORRECT_ID))
                    .thenReturn(Optional.of(comment));

            assertThrows(CustomAccessException.class, () -> commentService.updatePath(CORRECT_ID, requestDto));
            verify(commentMapper, never()).toDomain(any(CommentRequestDto.class));
            verify(commentMapper, never()).merge(any(Comment.class), any(Comment.class));
        }

        @Test
        void updateShouldUpdateComment_whenCorrectIdAndNameAndDtoTransferred() {
            String text = "new comment text";
            Comment comment = CommentTestBuilder.builder()
                    .build()
                    .buildComment();
            Optional<Comment> optionalComment = Optional.of(comment);
            CommentAndNamePathRequestDto requestDto = CommentTestBuilder.builder()
                    .withText(text)
                    .withTime(null)
                    .build()
                    .buildCommentAndNamePathRequestDto();
            CommentPathRequestDto commentPathRequestDto = requestDto.getRequestDto();
            Comment updated = CommentTestBuilder.builder()
                    .withText(text)
                    .withTime(null)
                    .build()
                    .buildComment();
            Comment merged = CommentTestBuilder.builder()
                    .withText(text)
                    .build()
                    .buildComment();

            when(commentRepository.findById(CORRECT_ID))
                    .thenReturn(optionalComment);
            when(commentMapper.toDomain(commentPathRequestDto))
                    .thenReturn(updated);
            when(commentMapper.merge(comment, updated))
                    .thenReturn(merged);

            commentService.updatePath(CORRECT_ID, requestDto);
        }

        @Test
        void updateShouldUpdateComment_whenCorrectIdAndDtoTransferredAndUserIsAdmin() {
            String text = "new comment text";
            Comment comment = CommentTestBuilder.builder()
                    .build()
                    .buildComment();
            Optional<Comment> optionalComment = Optional.of(comment);
            CommentAndNamePathRequestDto requestDto = CommentTestBuilder.builder()
                    .withText(text)
                    .withTime(null)
                    .build()
                    .buildCommentAndNamePathRequestDto();
            requestDto.setName("!ADMIN");
            CommentPathRequestDto commentPathRequestDto = requestDto.getRequestDto();
            Comment updated = CommentTestBuilder.builder()
                    .withText(text)
                    .withTime(null)
                    .build()
                    .buildComment();
            Comment merged = CommentTestBuilder.builder()
                    .withText(text)
                    .build()
                    .buildComment();

            when(commentRepository.findById(CORRECT_ID))
                    .thenReturn(optionalComment);
            when(commentMapper.toDomain(commentPathRequestDto))
                    .thenReturn(updated);
            when(commentMapper.merge(comment, updated))
                    .thenReturn(merged);

            commentService.updatePath(CORRECT_ID, requestDto);
        }
    }

    @Nested
    class DeleteTest {

        @Test
        void deleteShouldDeleteComment_whenCorrectIdAndUsername() {
            Comment comment = CommentTestBuilder.builder()
                    .build()
                    .buildComment();
            ;

            when(commentRepository.findById(CORRECT_ID))
                    .thenReturn(Optional.of(comment));

            commentService.delete(CORRECT_ID, AUTHOR_NAME);
        }

        @Test
        void deleteShouldDeleteComment_whenCorrectIdAndUserIsAdmin() {
            Comment comment = CommentTestBuilder.builder()
                    .build()
                    .buildComment();
            String admin = "!ADMIN";

            when(commentRepository.findById(CORRECT_ID))
                    .thenReturn(Optional.of(comment));

            commentService.delete(CORRECT_ID, admin);
        }

        @Test
        void deleteShouldThrowCustomEntityNotFoundException_whenIncorrectId() {
            CustomEntityNotFoundException expectedException = CustomEntityNotFoundException.of(Comment.class, INCORRECT_ID);

            when(commentRepository.findById(INCORRECT_ID))
                    .thenReturn(Optional.empty());

            CustomEntityNotFoundException actualException = assertThrows(CustomEntityNotFoundException.class, () -> commentService.delete(INCORRECT_ID, AUTHOR_NAME));

            assertEquals(expectedException.getMessage(), actualException.getMessage());
            verify(commentRepository, never()).delete(any(Comment.class));
        }

        @Test
        void deleteShouldCustomAccessException_whenHasNotRightToModify() {
            Comment comment = CommentTestBuilder.builder()
                    .build()
                    .buildComment();
            String name = "Someone Else";
            CustomAccessException expectedException = CustomAccessException.of(Comment.class);

            when(commentRepository.findById(CORRECT_ID))
                    .thenReturn(Optional.of(comment));

            CustomAccessException actualException = assertThrows(CustomAccessException.class, () -> commentService.delete(CORRECT_ID, name));

            assertEquals(expectedException.getMessage(), actualException.getMessage());
            verify(commentRepository, never()).delete(any(Comment.class));
        }
    }

    @Nested
    class TestFindCommentSearchResult {

        private final PageRequest pageRequest = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);

        @Test
        void getCommentSearchResultShouldReturnPageWithCommentResponseDto_whenValidConditionPassed() {
            CommentResponseDto commentResponseDto = CommentTestBuilder.builder()
                    .build()
                    .buildCommentResponseDto();
            List<CommentResponseDto> expected = List.of(commentResponseDto);
            Comment comment = CommentTestBuilder.builder()
                    .build()
                    .buildComment();
            List<Comment> commentList = List.of(comment);
            PageImpl<Comment> page = new PageImpl<>(commentList);
            String condition = "valid condition";

            when(commentRepository.getCommentsSearchResult(condition, pageRequest))
                    .thenReturn(page);
            when(commentMapper.toDto(comment))
                    .thenReturn(commentResponseDto);

            Page<CommentResponseDto> actual = commentService.findCommentsSearchResult(condition, pageRequest);

            assertThat(actual.getContent())
                    .hasSize(expected.size())
                    .isEqualTo(expected);
        }

        @Test
        void getCommentSearchResultShouldThrowCustomNoContentException_whenFoundedCommentsListIsEmpty() {
            CustomNoContentException expectedException = CustomNoContentException.of(Comment.class);
            List<Comment> comments = List.of();
            PageImpl<Comment> page = new PageImpl<>(comments);
            String condition = "invalid condition";

            when(commentRepository.getCommentsSearchResult(condition, pageRequest))
                    .thenReturn(page);

            CustomNoContentException actualException = assertThrows(CustomNoContentException.class, () -> commentService.findCommentsSearchResult(condition, pageRequest));

            assertEquals(expectedException.getMessage(), actualException.getMessage());
            verify(commentMapper, never()).toDto(any(Comment.class));
        }
    }

    @Nested
    class TestGetAllByNewsId {

        private final PageRequest pageRequest = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);

        @Test
        void getAllByNewsIdShouldReturnPageWithCommentResponseDtosList_whenFoundedComments() {
            Comment comment = CommentTestBuilder.builder()
                    .build()
                    .buildComment();
            CommentResponseDto expected = CommentTestBuilder.builder()
                    .build()
                    .buildCommentResponseDto();
            List<Comment> comments = List.of(comment);
            PageImpl<Comment> page = new PageImpl<>(comments);

            when(commentRepository.findByNewsId(NEWS_ID, pageRequest))
                    .thenReturn(page);
            when(commentMapper.toDto(comment))
                    .thenReturn(expected);

            Page<CommentResponseDto> actual = commentService.getAllByNewsId(NEWS_ID, pageRequest);

            assertThat(actual.getContent().get(0))
                    .hasFieldOrPropertyWithValue(Comment.Fields.time, expected.getTime())
                    .hasFieldOrPropertyWithValue(Comment.Fields.text, expected.getText())
                    .hasFieldOrPropertyWithValue(Comment.Fields.username, expected.getUsername())
                    .hasFieldOrPropertyWithValue("newsId", expected.getNewsId())
                    .hasFieldOrPropertyWithValue("author", expected.getAuthor());
        }

        @Test
        void getAllByNewsIdShouldThrowCustomNoContentException_whenFoundedCommentsListEmpty() {
            CustomNoContentException expectedException = CustomNoContentException.of(Comment.class);
            List<Comment> comments = List.of();
            PageImpl<Comment> page = new PageImpl<>(comments);

            when(commentRepository.findByNewsId(INCORRECT_ID, pageRequest))
                    .thenReturn(page);

            CustomNoContentException actualException = assertThrows(CustomNoContentException.class, () -> commentService.getAllByNewsId(INCORRECT_ID, pageRequest));

            assertEquals(expectedException.getMessage(), actualException.getMessage());
            verify(commentMapper, never()).toDto(any(Comment.class));
        }
    }

}
