package by.clevertec.auth.dto;//package by.clevertec.auth.dto;
//
//import by.clevertec.message.BaseResponse;
//import io.swagger.v3.oas.annotations.media.Schema;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//import org.springframework.http.HttpStatus;
//
//@Getter
//@Setter
//@NoArgsConstructor
//public class ExceptionResponse extends BaseResponse {
//
//    @Schema(description = "Exception response message", example = "Some exception")
//    private String message;
//    @Schema(description = "Exception type", example = "Some exception type")
//    private String type;
//
//    public ExceptionResponse(HttpStatus status, String message, String type) {
//        super(status.value());
//        this.message = message;
//        this.type = type;
//    }
//}
