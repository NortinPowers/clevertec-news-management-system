package by.clevertec.auth.validator;

import by.clevertec.auth.dto.UserRegistrationDto;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PasswordMatchingValidatorTest {

    @Mock
    private ConstraintValidatorContext constraintValidatorContext;

    private final PasswordMatchingValidator passwordMatchingValidator;

    private final UserRegistrationDto userRegistrationDto;

    {
        passwordMatchingValidator = new PasswordMatchingValidator();
        String password = "secret";
        userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setPassword(password);
        userRegistrationDto.setVerifyPassword(password);
    }

    @Test
    void test_isValid_confirmation() {
        assertTrue(passwordMatchingValidator.isValid(userRegistrationDto, constraintValidatorContext));
    }

    @Test
    void test_isValid_refutation() {
        userRegistrationDto.setVerifyPassword("some");

        assertFalse(passwordMatchingValidator.isValid(userRegistrationDto, constraintValidatorContext));
    }
}
