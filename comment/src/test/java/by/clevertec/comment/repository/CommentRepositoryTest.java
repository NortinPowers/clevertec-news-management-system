package by.clevertec.comment.repository;

import by.clevertec.comment.config.TestContainerConfig;
import by.clevertec.comment.domain.Comment;
import by.clevertec.comment.util.CommentTestBuilder;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static by.clevertec.comment.util.TestConstant.COMMENT_ID;
import static by.clevertec.comment.util.TestConstant.COMMENT_USERNAME;
import static by.clevertec.comment.util.TestConstant.NEWS_ID;
import static by.clevertec.comment.util.TestConstant.PAGE_NUMBER;
import static by.clevertec.comment.util.TestConstant.PAGE_SIZE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test")
@Transactional
@RequiredArgsConstructor
@Import(TestContainerConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)

//TODO @Transactional не работает?

@Sql(value = "classpath:sql/insert-before.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)

@Sql(value = "classpath:sql/reset-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class CommentRepositoryTest {

    private final CommentRepository commentRepository;

    @Test
    void findAllShouldReturnPageWithComments_whenCommentTableIsNotEmpty() {
        Comment expected = CommentTestBuilder.builder()
                .build()
                .buildComment();
        PageRequest pageRequest = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);

        Page<Comment> actual = commentRepository.findAll(pageRequest);

        assertThat(actual.getContent().get(0))
                .hasFieldOrPropertyWithValue(Comment.Fields.id, expected.getId())
                .hasFieldOrPropertyWithValue(Comment.Fields.time, expected.getTime())
                .hasFieldOrPropertyWithValue(Comment.Fields.text, expected.getText())
                .hasFieldOrPropertyWithValue(Comment.Fields.username, expected.getUsername());
        assertEquals(expected.getAuthor().getName(), actual.getContent().get(0).getAuthor().getName());
        assertEquals(expected.getNews().getTitle(), actual.getContent().get(0).getNews().getTitle());
    }

    @Test
    void findByIdShouldReturnComment_whenCommentExistInTable() {
        Comment expected = CommentTestBuilder.builder()
                .build()
                .buildComment();

        Optional<Comment> actual = commentRepository.findById(COMMENT_ID);

        assertTrue(actual.isPresent());
        assertThat(actual.get())
                .hasFieldOrPropertyWithValue(Comment.Fields.id, expected.getId())
                .hasFieldOrPropertyWithValue(Comment.Fields.time, expected.getTime())
                .hasFieldOrPropertyWithValue(Comment.Fields.text, expected.getText())
                .hasFieldOrPropertyWithValue(Comment.Fields.username, expected.getUsername());
        assertEquals(expected.getAuthor().getName(), actual.get().getAuthor().getName());
        assertEquals(expected.getNews().getTitle(), actual.get().getNews().getTitle());
    }

    @Test
    //TODO
//    @Sql(value = {"classpath:sql/comment/create-comment-without-owner.sql", "classpath:sql/delete-owners.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void deleteByIdShouldDeleteComment_whenCommentExistInTable() {
        Optional<Comment> before = commentRepository.findById(COMMENT_ID);
        assertTrue(before.isPresent());

        commentRepository.deleteById(COMMENT_ID);

        Optional<Comment> after = commentRepository.findById(COMMENT_ID);
        assertFalse(after.isPresent());
    }

    @Test
    void getCommentSearchResultShouldReturnPageOfComments_whenCommentWithSearchConditionExistInTable() {
        Comment expected = CommentTestBuilder.builder()
                .build()
                .buildComment();
        PageRequest pageRequest = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);

        Page<Comment> actual = commentRepository.getCommentsSearchResult(COMMENT_USERNAME, pageRequest);

        assertThat(actual.getContent().get(0))
                .hasFieldOrPropertyWithValue(Comment.Fields.id, expected.getId())
                .hasFieldOrPropertyWithValue(Comment.Fields.time, expected.getTime())
                .hasFieldOrPropertyWithValue(Comment.Fields.text, expected.getText())
                .hasFieldOrPropertyWithValue(Comment.Fields.username, expected.getUsername());
        assertEquals(expected.getAuthor().getName(), actual.getContent().get(0).getAuthor().getName());
        assertEquals(expected.getNews().getTitle(), actual.getContent().get(0).getNews().getTitle());
    }

    @Test
    void getCommentSearchResultShouldReturnPageWithEmptyOptional_whenCommentWithSearchConditionNotExistInTable() {
        PageRequest pageRequest = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);

        Page<Comment> actual = commentRepository.getCommentsSearchResult("some", pageRequest);

        Optional<Comment> commentOptional = actual.getContent().stream()
                .findFirst();

        assertThat(commentOptional).isEmpty();
    }

    @Test
    public void saveShouldNotThrowException_whenCalled() {
        Comment comment = CommentTestBuilder.builder()
                .build()
                .buildComment();

        assertDoesNotThrow(() -> commentRepository.save(comment));
    }

    @Test
    void findByNewsIdShouldReturnPageOfComments_whenCommentByNewsExist() {
        Comment expected = CommentTestBuilder.builder()
                .build()
                .buildComment();
        PageRequest pageRequest = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);

        Page<Comment> actual = commentRepository.findByNewsId(NEWS_ID, pageRequest);

        assertThat(actual.getContent().get(0))
                .hasFieldOrPropertyWithValue(Comment.Fields.id, expected.getId())
                .hasFieldOrPropertyWithValue(Comment.Fields.time, expected.getTime())
                .hasFieldOrPropertyWithValue(Comment.Fields.text, expected.getText())
                .hasFieldOrPropertyWithValue(Comment.Fields.username, expected.getUsername());
        assertEquals(expected.getAuthor().getName(), actual.getContent().get(0).getAuthor().getName());
        assertEquals(expected.getNews().getTitle(), actual.getContent().get(0).getNews().getTitle());
    }
}
