package by.clevertec.validator;

import by.clevertec.request.CommentRequestDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

//@RequiredArgsConstructor
public class CommentRequestDtoFieldValidator implements ConstraintValidator<CommentRequestDtoFieldValue, CommentRequestDto> {

    @Override
    public boolean isValid(CommentRequestDto requestDto, ConstraintValidatorContext constraintValidatorContext) {
        return requestDto.getText() != null
                && requestDto.getUsername() != null
                && requestDto.getNewsId() != null
                && !requestDto.getText().isEmpty()
                && !requestDto.getUsername().isEmpty()
                && requestDto.getNewsId() > 0;
    }
}
