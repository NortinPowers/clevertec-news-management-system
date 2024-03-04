package by.clevertec.auth.validator;

import by.clevertec.auth.service.UserService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserExistValidator implements ConstraintValidator<UserExist, String> {

    private final UserService userService;

    @Override
    public boolean isValid(String username, ConstraintValidatorContext constraintValidatorContext) {
        return !userService.isUserExist(username);
    }
}
