package by.clevertec.message;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@ToString
public class ErrorValidationResponse extends BaseResponse {

    @Schema(description = "List of validation errors", example = "[\"The 'author' field is required\", \"Some validation error\"]")
    private List<String> errors;

    @Schema(description = "Error validation message", example = "Some validation exception")
    private String message;

    public ErrorValidationResponse(HttpStatus status, List<String> errors, String message) {
        super(status.value());
        this.errors = errors;
        this.message = message;
    }
}
