package by.clevertec.validator;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = NewsRequestDtoFieldValidator.class)
@Target(FIELD)
@Retention(RUNTIME)
public @interface NewsRequestDtoFieldValue {

    String message() default "News is not valid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
