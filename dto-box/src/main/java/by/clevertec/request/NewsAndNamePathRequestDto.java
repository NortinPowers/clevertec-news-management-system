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
public class NewsAndNamePathRequestDto {

    @Schema(description = "name", example = "John Gold")
    private String name;

    @Schema(description = "request dto object")
    private NewsPathRequestDto requestDto;
}
