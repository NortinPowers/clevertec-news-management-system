package by.clevertec.util;

import static by.clevertec.util.ResponseUtils.getErrorValidationMessages;
import static by.clevertec.util.ResponseUtils.getExceptionResponse;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import by.clevertec.model.ExceptionResponse;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

class ResponseUtilsTest {

    @Test
    void getExceptionResponseShouldReturnExceptionResponseByTransmittedError() {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String message = "Something went wrong";
        Exception exception = new NullPointerException();

        ExceptionResponse actual = getExceptionResponse(status, message, exception);

        assertNotNull(actual.getTimestamp());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), actual.getStatus());
        assertEquals(message, actual.getMessage());
        assertEquals(exception.getClass().getSimpleName(), actual.getType());
    }

    @Test
    void getErrorValidationMessagesShouldReturnErrorValidationMessagesDependedOnBindingResult() {
        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(null, new BeanPropertyBindingResult(null, "objectName"));
        exception.getBindingResult().addError(new FieldError("objectName", "fieldName", "Field is required"));
        exception.getBindingResult().addError(new FieldError("objectName", "otherField", "Other field is invalid"));

        List<String> errorMessages = getErrorValidationMessages(exception);

        assertThat(errorMessages)
                .containsExactly("Field is required", "Other field is invalid");
    }
}
