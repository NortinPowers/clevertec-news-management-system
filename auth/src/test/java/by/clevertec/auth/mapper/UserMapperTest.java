package by.clevertec.auth.mapper;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import by.clevertec.auth.UserDto;
import by.clevertec.auth.domain.User;
import by.clevertec.auth.dto.UserRegistrationDto;
import by.clevertec.auth.util.UserTestBuilder;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@RequiredArgsConstructor
class UserMapperTest {

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
