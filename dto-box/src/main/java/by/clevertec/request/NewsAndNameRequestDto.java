package by.clevertec.request;

import by.clevertec.validator.NewsRequestDtoFieldValue;
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
public class NewsAndNameRequestDto {

    @NotBlank(message = "Enter name")
    @Schema(description = "name", example = "John Gold")
    private String name;

    @NewsRequestDtoFieldValue
    @Schema(description = "request dto object")
    private NewsRequestDto requestDto;
}
