package by.clevertec.auth.handler;

import static by.clevertec.auth.utils.Constants.ENTITY_NOT_FOUND_EXCEPTION_MESSAGE;
import static by.clevertec.auth.utils.ResponseUtils.BAD_CREDENTIALS_EXCEPTION_MESSAGE;
import static by.clevertec.auth.utils.ResponseUtils.DATA_INTEGRITY_VIOLATION_EXCEPTION_MESSAGE;
import static by.clevertec.auth.utils.ResponseUtils.DATA_SOURCE_LOOKUP_FAILURE_EXCEPTION_MESSAGE;
import static by.clevertec.auth.utils.ResponseUtils.HTTP_MESSAGE_NOT_READABLE_EXCEPTION_MESSAGE;
import static by.clevertec.auth.utils.ResponseUtils.JPA_OBJECT_RETRIEVAL_FAILURE_EXCEPTION_MESSAGE;
import static by.clevertec.auth.utils.ResponseUtils.METHOD_ARGUMENT_NOT_VALID_EXCEPTION_MESSAGE;
import static by.clevertec.auth.utils.ResponseUtils.getErrorsValidationResponse;
import static by.clevertec.auth.utils.ResponseUtils.getExceptionResponse;

import by.clevertec.message.BaseResponse;
import by.clevertec.message.ErrorValidationResponse;
import by.clevertec.message.ExceptionResponse;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.jdbc.datasource.lookup.DataSourceLookupFailureException;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Component
@RestControllerAdvice
public class CustomExceptionHandler {

    /**
     * Обрабатывает исключение {@link BadCredentialsException} и возвращает соответствующий ResponseEntity с {@link BaseResponse}.
     *
     * @param exception Исключение {@link BadCredentialsException}, которое требуется обработать.
     * @return ResponseEntity с {@link BaseResponse} и кодом состояния HTTP UNAUTHORIZED.
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<BaseResponse> handleException(BadCredentialsException exception) {
        ExceptionResponse response = getExceptionResponse(
                HttpStatus.UNAUTHORIZED,
                BAD_CREDENTIALS_EXCEPTION_MESSAGE,
                exception
        );
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Обрабатывает исключение {@link MethodArgumentNotValidException} и возвращает соответствующий ResponseEntity с {@link BaseResponse}.
     *
     * @param exception Исключение {@link MethodArgumentNotValidException}, которое требуется обработать.
     * @return ResponseEntity с {@link BaseResponse} и кодом состояния HTTP BAD_REQUEST.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse> handleException(MethodArgumentNotValidException exception) {
        ErrorValidationResponse response = new ErrorValidationResponse(
                HttpStatus.BAD_REQUEST,
                getErrorsValidationResponse(exception),
                METHOD_ARGUMENT_NOT_VALID_EXCEPTION_MESSAGE
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Обрабатывает исключение {@link DataSourceLookupFailureException} и возвращает соответствующий ResponseEntity с {@link BaseResponse}.
     *
     * @param exception Исключение {@link DataSourceLookupFailureException}, которое требуется обработать.
     * @return ResponseEntity с {@link BaseResponse} и кодом состояния HTTP INTERNAL_SERVER_ERROR.
     */
    @ExceptionHandler(DataSourceLookupFailureException.class)
    public ResponseEntity<BaseResponse> handleException(DataSourceLookupFailureException exception) {
        ExceptionResponse response = getExceptionResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                DATA_SOURCE_LOOKUP_FAILURE_EXCEPTION_MESSAGE,
                exception
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Обрабатывает исключение {@link DataIntegrityViolationException} и возвращает соответствующий ResponseEntity с {@link BaseResponse}.
     *
     * @param exception Исключение {@link DataIntegrityViolationException}, которое требуется обработать.
     * @return ResponseEntity с {@link BaseResponse} и кодом состояния HTTP INTERNAL_SERVER_ERROR.
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<BaseResponse> handleException(DataIntegrityViolationException exception) {
        ExceptionResponse response = getExceptionResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                DATA_INTEGRITY_VIOLATION_EXCEPTION_MESSAGE,
                exception
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Обрабатывает исключение {@link HttpMessageNotReadableException} и возвращает соответствующий ResponseEntity с {@link BaseResponse}.
     *
     * @param exception Исключение {@link HttpMessageNotReadableException}, которое требуется обработать.
     * @return ResponseEntity с {@link BaseResponse} и кодом состояния HTTP INTERNAL_SERVER_ERROR.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<BaseResponse> handleException(HttpMessageNotReadableException exception) {
        ExceptionResponse response = getExceptionResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                HTTP_MESSAGE_NOT_READABLE_EXCEPTION_MESSAGE,
                exception
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Обрабатывает исключение {@link EntityNotFoundException} и возвращает соответствующий ResponseEntity с {@link BaseResponse}.
     *
     * @param exception Исключение {@link EntityNotFoundException}, которое требуется обработать.
     * @return ResponseEntity с {@link BaseResponse} и кодом состояния HTTP NOT_FOUND.
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<BaseResponse> handleException(EntityNotFoundException exception) {
        ExceptionResponse response = getExceptionResponse(
                HttpStatus.NOT_FOUND,
                ENTITY_NOT_FOUND_EXCEPTION_MESSAGE,
                exception
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    /**
     * Обрабатывает исключение {@link JpaObjectRetrievalFailureException} и возвращает соответствующий ResponseEntity с {@link BaseResponse}.
     *
     * @param exception Исключение {@link JpaObjectRetrievalFailureException}, которое требуется обработать.
     * @return ResponseEntity с {@link BaseResponse} и кодом состояния HTTP INTERNAL_SERVER_ERROR.
     */
    @ExceptionHandler(JpaObjectRetrievalFailureException.class)
    public ResponseEntity<BaseResponse> handleException(JpaObjectRetrievalFailureException exception) {
        ExceptionResponse response = getExceptionResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                JPA_OBJECT_RETRIEVAL_FAILURE_EXCEPTION_MESSAGE,
                exception
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Обрабатывает исключение {@link AccessDeniedException} и возвращает соответствующий ResponseEntity с {@link BaseResponse}.
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
}
