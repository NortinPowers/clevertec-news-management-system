package by.clevertec.message;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldNameConstants;

@Getter
@Setter
@ToString
@NoArgsConstructor
//@FieldNameConstants
public class MessageResponse extends BaseResponse {

    @Schema(description = "Success response message", example = "Message about any success")
    private String message;

    @JsonIgnore
    private Object object;

    public MessageResponse(Integer status, String message, Object object) {
        super(status);
        this.message = message;
        this.object = object;
    }
}
