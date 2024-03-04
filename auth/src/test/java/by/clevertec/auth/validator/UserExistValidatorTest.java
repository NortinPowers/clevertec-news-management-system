package by.clevertec.auth.validator;

import by.clevertec.auth.service.UserService;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserExistValidatorTest {

    @Mock
    private UserService userService;

    @Mock
    private ConstraintValidatorContext constraintValidatorContext;

    @InjectMocks
    private UserExistValidator userExistValidator;

    private final String name = "Test";
    private boolean flag;
//
//    {
//        name = "Test";
//    }

    @Test
    void test_isValid_confirmation() {
        when(userService.isUserExist(name))
                .thenReturn(flag);

        assertTrue(userExistValidator.isValid(name, constraintValidatorContext));
    }

    @Test
    void test_isValid_refutation() {
        flag = true;

        when(userService.isUserExist(name))
                .thenReturn(flag);

        assertFalse(userExistValidator.isValid(name, constraintValidatorContext));
    }
}
