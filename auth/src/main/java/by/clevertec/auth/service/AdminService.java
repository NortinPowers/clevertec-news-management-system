package by.clevertec.auth.service;


import by.clevertec.auth.UserDto;
import java.util.List;

public interface AdminService {

    void setAdmin(Long id);

    List<UserDto> getAllUsers();
}
