package by.clevertec.auth.mapper;

import by.clevertec.auth.UserDto;
import by.clevertec.auth.domain.User;
import by.clevertec.auth.dto.UserRegistrationDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = BaseMapper.class)
public interface UserMapper {

    /**
     * Преобразует данные о регистрации пользователя в объект доменной модели.
     *
     * @param userRegistrationDto Данные о регистрации пользователя.
     * @return Объект {@link User}, представляющий пользователя.
     */
    User toDomain(UserRegistrationDto userRegistrationDto);

    /**
     * Преобразует объект пользователя в объект передачи данных (DTO).
     *
     * @param user Пользователь для преобразования.
     * @return Объект {@link UserDto}, содержащий данные о пользователе.
     */
    @Mapping(target = "role", source = "user.role.name")
    UserDto toDto(User user);
}
