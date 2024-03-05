package by.clevertec.auth.dto;

import static by.clevertec.auth.utils.Constants.PASSWORD_NOT_BLANK;
import static by.clevertec.auth.utils.Constants.PASSWORD_PATTERN;
import static by.clevertec.auth.utils.Constants.USERNAME_NOT_BLANK;
import static by.clevertec.auth.utils.Constants.USERNAME_PATTERN;

import by.clevertec.auth.validator.PasswordMatching;
import by.clevertec.auth.validator.UserExist;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Valid
@Getter
@Setter
@PasswordMatching
@Schema(description = "Entity of User")
public class UserRegistrationDto implements Serializable {

    @UserExist
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
