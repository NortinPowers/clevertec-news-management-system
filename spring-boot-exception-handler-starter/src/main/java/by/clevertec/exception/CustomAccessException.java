package by.clevertec.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class CustomAccessException extends RuntimeException {

    public CustomAccessException(String message) {
        super(message);
    }

    public static CustomAccessException of(Class<?> clazz) {
        return new CustomAccessException("You do not have the rights to make changes in the " + clazz.getSimpleName());
    }
}
