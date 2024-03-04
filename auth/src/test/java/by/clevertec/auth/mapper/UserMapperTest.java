package by.clevertec.auth.mapper;

import by.clevertec.auth.AbstractTest;
import by.clevertec.auth.UserDto;
import by.clevertec.auth.domain.BaseDomain;
import by.clevertec.auth.domain.User;
import by.clevertec.auth.dto.UserRegistrationDto;
import by.clevertec.auth.util.UserTestBuilder;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RequiredArgsConstructor
class UserMapperTest extends AbstractTest {

    private final UserMapper userMapper;

    @Test
    void toDtoShouldReturnUserResponseDto_whenUserPassed() {
        User user = UserTestBuilder.builder()
                .build()
                .buildUser();
        UserDto expected = UserTestBuilder.builder()
                .build()
                .buildUserDto();

        UserDto actual = userMapper.toDto(user);

        assertThat(actual)
                .hasFieldOrPropertyWithValue(User.Fields.username, expected.getUsername())
                .hasFieldOrPropertyWithValue("role", expected.getRole());
    }

    @Test
    void userDtoToDomainShouldReturnUser_whenUserDtoPassed() {
        UserRegistrationDto userDto = UserTestBuilder.builder()
                .build()
                .buildUserRegistrationDto();
        User expected = UserTestBuilder.builder()
                .build()
                .buildUser();

        User actual = userMapper.toDomain(userDto);

        assertThat(actual)
                .hasFieldOrPropertyWithValue(User.Fields.username, expected.getUsername())
                .hasFieldOrPropertyWithValue(User.Fields.password, expected.getPassword());
    }
}
