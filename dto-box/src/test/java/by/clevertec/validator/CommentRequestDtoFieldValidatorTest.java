package by.clevertec.validator;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import by.clevertec.request.CommentRequestDto;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Test;

public class CommentRequestDtoFieldValidatorTest {

    private final CommentRequestDtoFieldValidator validator = new CommentRequestDtoFieldValidator();

    @Test
    public void isValidShouldReturnTrue_whenValidRequestDto() {
        CommentRequestDto requestDto = new CommentRequestDto();
        requestDto.setText("some text");
        requestDto.setUsername("name");
        requestDto.setNewsId(1L);

        assertTrue(validator.isValid(requestDto, mock(ConstraintValidatorContext.class)));
    }

    @Test
    public void isValidShouldReturnFalse_whenRequestDtoNameNull() {
        CommentRequestDto requestDto = new CommentRequestDto();
        requestDto.setText("some text");
        requestDto.setNewsId(1L);

        assertFalse(validator.isValid(requestDto, mock(ConstraintValidatorContext.class)));
    }

    @Test
    public void isValidShouldReturnFalse_whenRequestDtoTextNull() {
        CommentRequestDto requestDto = new CommentRequestDto();
        requestDto.setUsername("name");
        requestDto.setNewsId(1L);

        assertFalse(validator.isValid(requestDto, mock(ConstraintValidatorContext.class)));
    }

    @Test
    public void isValidShouldReturnFalse_whenRequestDtoNewsIdNull() {
        CommentRequestDto requestDto = new CommentRequestDto();
        requestDto.setText("some text");
        requestDto.setUsername("name");

        assertFalse(validator.isValid(requestDto, mock(ConstraintValidatorContext.class)));
    }

    @Test
    public void isValidShouldReturnFalse_whenRequestDtoNameEmpty() {
        CommentRequestDto requestDto = new CommentRequestDto();
        requestDto.setUsername("");
        requestDto.setText("some text");
        requestDto.setNewsId(1L);

        assertFalse(validator.isValid(requestDto, mock(ConstraintValidatorContext.class)));
    }

    @Test
    public void isValidShouldReturnFalse_whenRequestDtoTextEmpty() {
        CommentRequestDto requestDto = new CommentRequestDto();
        requestDto.setUsername("name");
        requestDto.setText("");
        requestDto.setNewsId(1L);

        assertFalse(validator.isValid(requestDto, mock(ConstraintValidatorContext.class)));
    }

    @Test
    public void isValidShouldReturnFalse_whenRequestDtoNewsIdInvalid() {
        CommentRequestDto requestDto = new CommentRequestDto();
        requestDto.setUsername("name");
        requestDto.setText("text");
        requestDto.setNewsId(0L);

        assertFalse(validator.isValid(requestDto, mock(ConstraintValidatorContext.class)));
    }
}
