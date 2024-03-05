package by.clevertec.auth;

import static by.clevertec.utils.Constants.ROLE_USER;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description = "Entity of User")
public class UserDto extends BaseDto {

    @Schema(description = "Username", example = "Samael")
    private String username;

    @Schema(description = "User`s role", example = ROLE_USER)
    private String role;
}
