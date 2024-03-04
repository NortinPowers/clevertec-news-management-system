package by.clevertec.auth;

import static by.clevertec.utils.Constants.PASSWORD_NOT_BLANK;
import static by.clevertec.utils.Constants.PASSWORD_PATTERN;
import static by.clevertec.utils.Constants.USERNAME_NOT_BLANK;
import static by.clevertec.utils.Constants.USERNAME_PATTERN;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Entity of User")
public class UserRegistrationDto implements Serializable {

    @NotBlank(message = USERNAME_NOT_BLANK)
    @Schema(description = "Username", example = "Samael")
    @Pattern(regexp = USERNAME_PATTERN, message = "Incorrect username")
    private String username;

    @NotBlank(message = PASSWORD_NOT_BLANK)
    @Schema(description = "Password", example = "rebel")
    @Pattern(regexp = PASSWORD_PATTERN, message = "Incorrect password")
    private String password;

    @NotBlank(message = "Enter verify password")
    @Schema(description = "Password", example = "rebel")
    @Pattern(regexp = PASSWORD_PATTERN, message = "Incorrect  verify password")
    private String verifyPassword;
}
