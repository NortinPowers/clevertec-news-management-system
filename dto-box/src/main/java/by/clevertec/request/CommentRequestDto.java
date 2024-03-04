package by.clevertec.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class CommentRequestDto {

    @NotBlank(message = "Enter text")
    @Schema(description = "text", example = "I haven't read anything better")
    private String text;

    @NotBlank(message = "Enter username")
    @Schema(description = "username", example = "Hank Rearden")
    private String username;

    @Schema(description = "News identifier", example = "1")
    private Long newsId;
}
