package by.clevertec.gateway.handler;


import static by.clevertec.gateway.utils.Constants.HandlerConstants.MESSAGE;
import static by.clevertec.gateway.utils.Constants.HandlerConstants.STATUS;
import static by.clevertec.gateway.utils.Constants.HandlerConstants.TYPE;
import static by.clevertec.gateway.utils.ResponseUtils.OTHER_EXCEPTION_MESSAGE;
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


    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<BaseResponse> handleException(AccessDeniedException exception) {
        ExceptionResponse response = getExceptionResponse(
                HttpStatus.FORBIDDEN,
                exception.getMessage(),
                exception
        );
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }


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

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<BaseResponse> handleException(RuntimeException exception) {
        ExceptionResponse response = getExceptionResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                OTHER_EXCEPTION_MESSAGE,
                exception
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private static RerouteExceptionResponse getRerouteExceptionResponse(FeignException exception) {
        String errorResponse = exception.contentUTF8();
        JsonObject jsonObject = JsonParser.parseString(errorResponse).getAsJsonObject();
        int status = jsonObject.get(STATUS).getAsInt();
        String message = jsonObject.get(MESSAGE).getAsString();
        String type = jsonObject.get(TYPE).getAsString();
        return new RerouteExceptionResponse(status, message, type);
    }

    record RerouteExceptionResponse(int status, String message, String type){};
}
