package by.clevertec.auth.service;


import by.clevertec.auth.UserDto;
import by.clevertec.auth.domain.User;
import by.clevertec.auth.dto.UserRegistrationDto;
import java.util.List;

public interface UserService {

    User getUserByUsername(String username);

    void save(UserRegistrationDto userRegistrationDto);

    boolean isUserExist(String username);

    void setRoleAdmin(Long id);

    List<UserDto> getAllUsers();
}
