package by.clevertec.auth.mapper;

import by.clevertec.auth.UserDto;
import by.clevertec.auth.domain.User;
import by.clevertec.auth.dto.UserRegistrationDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = BaseMapper.class)
public interface UserMapper {

    User toDomain(UserRegistrationDto userRegistrationDto);

    @Mapping(target = "role", source = "user.role.name")
    UserDto toDto(User user);
}
