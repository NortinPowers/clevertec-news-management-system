package by.clevertec.validator;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = CommentRequestDtoFieldValidator.class)
@Target(FIELD)
@Retention(RUNTIME)
public @interface CommentRequestDtoFieldValue {

    String message() default "Comment is not valid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
