package by.clevertec.gateway.util;



import static by.clevertec.gateway.util.TestConstant.USER_ID;
import static by.clevertec.gateway.util.TestConstant.USER_PASSWORD;
import static by.clevertec.gateway.util.TestConstant.USER_USERNAME;

import by.clevertec.auth.UserDto;
import by.clevertec.auth.UserRegistrationDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(setterPrefix = "with")
public class UserTestBuilder {

    @Builder.Default
    private Long id = USER_ID;

    @Builder.Default
    private String username = USER_USERNAME;
    
    @Builder.Default
    private String password = USER_PASSWORD;

    @Builder.Default
    private String roleName = "ADMIN";

    public UserDto buildUserDto() {
        UserDto user = new UserDto();
        user.setUsername(username);
        user.setRole(roleName);
        return user;
    }

    public UserRegistrationDto buildUserRegistrationDto() {
        UserRegistrationDto user = new UserRegistrationDto();
        user.setUsername(username);
        user.setPassword(password);
        user.setVerifyPassword(password);
        return user;
    }
}
