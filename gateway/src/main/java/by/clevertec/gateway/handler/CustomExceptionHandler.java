package by.clevertec.gateway.handler;

import static by.clevertec.gateway.utils.Constants.HandlerConstants.MESSAGE;
import static by.clevertec.gateway.utils.Constants.HandlerConstants.STATUS;
import static by.clevertec.gateway.utils.Constants.HandlerConstants.TYPE;
import static by.clevertec.utils.ResponseUtils.getExceptionResponse;

import by.clevertec.message.BaseResponse;
import by.clevertec.message.ExceptionResponse;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Component
@RestControllerAdvice
public class CustomExceptionHandler {

    /**
     * Обрабатывает исключение {@link AccessDeniedException} и возвращает соответствующий ResponseEntity с {@link by.clevertec.model.BaseResponse}.
     *
     * @param exception Исключение {@link AccessDeniedException}, которое требуется обработать.
     * @return ResponseEntity с {@link BaseResponse} и кодом состояния HTTP FORBIDDEN.
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<BaseResponse> handleException(AccessDeniedException exception) {
        ExceptionResponse response = getExceptionResponse(
                HttpStatus.FORBIDDEN,
                exception.getMessage(),
                exception
        );
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    /**
     * Обрабатывает исключение {@link FeignException} и возвращает соответствующий ResponseEntity с {@link by.clevertec.model.BaseResponse}.
     *
     * @param exception Исключение {@link FeignException}, которое требуется обработать.
     * @return ResponseEntity с {@link BaseResponse} и кодом состояния HTTP из перехваченной ошибки внешнего сервиса.
     */
    @ExceptionHandler(FeignException.class)
    public ResponseEntity<BaseResponse> handleException(FeignException exception) {
        RerouteExceptionResponse rerouteExceptionResponse = getRerouteExceptionResponse(exception);
        ExceptionResponse response = new ExceptionResponse(
                HttpStatus.valueOf(rerouteExceptionResponse.status),
                rerouteExceptionResponse.message,
                rerouteExceptionResponse.type
        );
        return new ResponseEntity<>(response, HttpStatus.valueOf(exception.status()));
    }

    /**
     * Обрабатывает исключение {@link RuntimeException} и возвращает соответствующий ResponseEntity с {@link by.clevertec.model.BaseResponse}.
     *
     * @param exception Исключение {@link RuntimeException}, которое требуется обработать.
     * @return ResponseEntity с {@link BaseResponse} и кодом состояния HTTP INTERNAL_SERVER_ERROR.
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<BaseResponse> handleException(RuntimeException exception) {
        ExceptionResponse response = getExceptionResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Unexpected error",
                exception
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Создает объект {@link RerouteExceptionResponse} на основе данных из исключения Feign.
     *
     * @param exception Исключение Feign.
     * @return Объект {@link RerouteExceptionResponse} с данными об ошибке.
     */
    private static RerouteExceptionResponse getRerouteExceptionResponse(FeignException exception) {
        String errorResponse = exception.contentUTF8();
        JsonObject jsonObject = JsonParser.parseString(errorResponse).getAsJsonObject();
        int status = jsonObject.get(STATUS).getAsInt();
        String message = jsonObject.get(MESSAGE).getAsString();
        String type = jsonObject.get(TYPE).getAsString();
        return new RerouteExceptionResponse(status, message, type);
    }

    record RerouteExceptionResponse(int status, String message, String type) {
    }
}
