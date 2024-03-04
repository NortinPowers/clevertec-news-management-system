package by.clevertec.auth.dto;//package by.clevertec.auth.dto;
//
//import by.clevertec.message.BaseResponse;
//
//import io.swagger.v3.oas.annotations.media.Schema;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//import org.springframework.http.HttpStatus;
//import java.util.List;
//
//@Getter
//@Setter
//@NoArgsConstructor
//public class ErrorValidatorResponse extends BaseResponse {
//
//    @Schema(description = "List of validation errors", example = "[\"The 'author' field is required\", \"Some validation error\"]")
//    private List<String> errors;
//    @Schema(description = "Error validation message", example = "Some validation exception")
//    private String message;
//
//    public ErrorValidatorResponse(HttpStatus status, List<String> errors, String message) {
//        super(status.value());
//        this.errors = errors;
//        this.message = message;
//    }
//}
