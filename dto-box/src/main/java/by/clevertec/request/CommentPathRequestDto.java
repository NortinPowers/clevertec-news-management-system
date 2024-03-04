package by.clevertec.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class CommentPathRequestDto {

    @Schema(description = "text", example = "I haven't read anything better")
    private String text;

    @Schema(description = "username", example = "Hank Rearden")
    private String username;

    @Schema(description = "News identifier", example = "1")
    private Long newsId;
}
