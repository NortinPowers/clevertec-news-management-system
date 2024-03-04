package by.clevertec.auth.util;


import by.clevertec.auth.UserDto;
import by.clevertec.auth.domain.Role;
import by.clevertec.auth.domain.User;
import by.clevertec.auth.dto.UserRegistrationDto;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

import static by.clevertec.auth.util.TestConstant.USER_ID;
import static by.clevertec.auth.util.TestConstant.USER_PASSWORD;
import static by.clevertec.auth.util.TestConstant.USER_USERNAME;

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
    private Role role = RoleTestBuilder.builder().build().buildRole();

    public User buildUser() {
        User user = new User();
        user.setId(id);
        user.setPassword(password);
        user.setUsername(username);
        user.setRole(role);
        return user;
    }

    public UserDto buildUserDto() {
        UserDto user = new UserDto();
        user.setUsername(username);
        user.setRole(role.getName());
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
