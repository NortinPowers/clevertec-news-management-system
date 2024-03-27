package by.clevertec.validator;


import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import by.clevertec.request.NewsRequestDto;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Test;

public class NewsRequestDtoFieldValidatorTest {

    private final NewsRequestDtoFieldValidator validator = new NewsRequestDtoFieldValidator();

    @Test
    public void isValidShouldReturnTrue_whenValidRequestDto() {
        NewsRequestDto requestDto = new NewsRequestDto();
        requestDto.setTitle("title");
        requestDto.setText("some text");

        assertTrue(validator.isValid(requestDto, mock(ConstraintValidatorContext.class)));
    }

    @Test
    public void isValidShouldReturnFalse_whenRequestDtoTitleNull() {
        NewsRequestDto requestDto = new NewsRequestDto();
        requestDto.setText("some text");

        assertFalse(validator.isValid(requestDto, mock(ConstraintValidatorContext.class)));
    }

    @Test
    public void isValidShouldReturnFalse_whenRequestDtoTextNull() {
        NewsRequestDto requestDto = new NewsRequestDto();
        requestDto.setTitle("title");

        assertFalse(validator.isValid(requestDto, mock(ConstraintValidatorContext.class)));
    }

    @Test
    public void isValidShouldReturnFalse_whenRequestDtoTitleEmpty() {
        NewsRequestDto requestDto = new NewsRequestDto();
        requestDto.setTitle("");
        requestDto.setText("some text");

        assertFalse(validator.isValid(requestDto, mock(ConstraintValidatorContext.class)));
    }

    @Test
    public void isValidShouldReturnFalse_whenRequestDtoTextEmpty() {
        NewsRequestDto requestDto = new NewsRequestDto();
        requestDto.setTitle("title");
        requestDto.setText("");

        assertFalse(validator.isValid(requestDto, mock(ConstraintValidatorContext.class)));
    }
}
