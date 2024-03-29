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
public class NewsRequestDto {

    @NotBlank(message = "Enter title")
    @Schema(description = "title", example = "An incredible event")
    private String title;

    @NotBlank(message = "Enter text")
    @Schema(description = "text", example = "This has never happened before and here it is again")
    private String text;
}
