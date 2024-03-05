package by.clevertec.validator;

import by.clevertec.request.NewsRequestDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NewsRequestDtoFieldValidator implements ConstraintValidator<NewsRequestDtoFieldValue, NewsRequestDto> {

    @Override
    public boolean isValid(NewsRequestDto requestDto, ConstraintValidatorContext constraintValidatorContext) {
        return requestDto.getTitle() != null
                && requestDto.getText() != null
                && !requestDto.getTitle().isEmpty()
                && !requestDto.getText().isEmpty();
    }
}
