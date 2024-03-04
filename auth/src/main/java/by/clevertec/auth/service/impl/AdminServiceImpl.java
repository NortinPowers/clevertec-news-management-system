package by.clevertec.auth.service.impl;


import by.clevertec.aspect.ServiceAspectLogger;
import by.clevertec.auth.UserDto;
import by.clevertec.auth.service.AdminService;
import by.clevertec.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserService userService;

    @Override
    @ServiceAspectLogger
    public void setAdmin(Long id) {
        userService.setRoleAdmin(id);
    }

    @Override
    @ServiceAspectLogger
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }
}
