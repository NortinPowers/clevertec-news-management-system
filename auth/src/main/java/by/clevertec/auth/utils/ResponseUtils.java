package by.clevertec.auth.utils;

import by.clevertec.message.ExceptionResponse;
import by.clevertec.message.MessageResponse;
import by.clevertec.message.SuccessResponse;
import java.util.List;
import lombok.experimental.UtilityClass;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;

@UtilityClass
public class ResponseUtils {

    public static final String CHANGE_ROLE_MESSAGE = "The role from %s have been successful changed";
    public static final String BAD_CREDENTIALS_EXCEPTION_MESSAGE = "Incorrect username and password";
    public static final String METHOD_ARGUMENT_NOT_VALID_EXCEPTION_MESSAGE = "The transmitted data did not pass verification";
    public static final String DATA_SOURCE_LOOKUP_FAILURE_EXCEPTION_MESSAGE = "Data source could not be obtained";
    public static final String DATA_INTEGRITY_VIOLATION_EXCEPTION_MESSAGE = "The input data does not correspond to the required";
    public static final String HTTP_MESSAGE_NOT_READABLE_EXCEPTION_MESSAGE = "The entered data is incorrect and leads to error";
    public static final String JPA_OBJECT_RETRIEVAL_FAILURE_EXCEPTION_MESSAGE = "The data entered violates the established requirements";
    public static final String MALFORMED_JWT_EXCEPTION_MESSAGE = "Incorrect token";
    public static final String EXPIRED_JWT_EXCEPTION_MESSAGE = "The lifecycle of the token is completed";

    /**
     * Возвращает объект {@link MessageResponse} для успешного ответа с указанным сообщением и
     * кодом состояния HTTP OK, предоставленным в виде строки.
     *
     * @param message   Сообщение для включения в ответ.
     * @param className Имя, используемое для формирования ответа.
     * @return Объект {@link MessageResponse} для успешного ответа.
     */
    public static SuccessResponse getSuccessResponse(String message, String className) {
        return new SuccessResponse(HttpStatus.OK, String.format(message, className.toLowerCase()), className);
    }

    /**
     * Возвращает объект {@link ExceptionResponse} для ответа с ошибкой, содержащий
     * указанный статус, сообщение и имя класса исключения.
     *
     * @param status    Статус ответа с ошибкой.
     * @param message   Сообщение для включения в ответ.
     * @param exception Исключение, используемое для получения имени класса.
     * @return Объект {@link ExceptionResponse} для ответа с ошибкой.
     */
    public static ExceptionResponse getExceptionResponse(HttpStatus status, String message, Exception exception) {
        return new ExceptionResponse(status, message, exception.getClass().getSimpleName());
    }

    /**
     * Возвращает список строк, представляющих сообщения об ошибках валидации
     * для объекта {@link MethodArgumentNotValidException}.
     *
     * @param exception Исключение типа {@link MethodArgumentNotValidException}.
     * @return Список строк с сообщениями об ошибках валидации.
     */
    public static List<String> getErrorsValidationResponse(MethodArgumentNotValidException exception) {
        return exception.getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();
    }
}
