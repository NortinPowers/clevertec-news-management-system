package by.clevertec.response;

import static by.clevertec.utils.Constants.AUTHOR_PATTERN;
import static by.clevertec.utils.Constants.USERNAME_COMMENT_PATTERN;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.io.Serializable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class CommentResponseDto implements Serializable {

    @NotBlank(message = "Enter time")
    @Schema(description = "time", example = "2024-02-01T14:00:00")
    private String time;

    @NotBlank(message = "Enter text")
    @Schema(description = "text", example = "Very interesting news text")
    private String text;

    @NotBlank(message = "Enter username")
    @Schema(description = "username", example = "Allan")
    @Pattern(regexp = USERNAME_COMMENT_PATTERN, message = "Incorrect username")
    private String username;

    @Min(1)
    @Schema(description = "News identifier", example = "1")
    private Long newsId;

    @NotBlank(message = "Enter author")
    @Schema(description = "author", example = "Stephen King")
    @Pattern(regexp = AUTHOR_PATTERN, message = "Incorrect author`s name")
    private String author;
}
