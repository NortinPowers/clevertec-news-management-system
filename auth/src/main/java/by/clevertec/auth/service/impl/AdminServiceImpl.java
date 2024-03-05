package by.clevertec.auth.service.impl;

import by.clevertec.aspect.ServiceAspectLogger;
import by.clevertec.auth.UserDto;
import by.clevertec.auth.service.AdminService;
import by.clevertec.auth.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserService userService;

    /**
     * Назначает роль администратора пользователю с указанным идентификатором.
     *
     * @param id Идентификатор пользователя, которому нужно назначить роль администратора.
     */
    @Override
    @ServiceAspectLogger
    public void setAdmin(Long id) {
        userService.setRoleAdmin(id);
    }

    /**
     * Назначает роль журналиста пользователю с указанным идентификатором.
     *
     * @param id Идентификатор пользователя, которому нужно назначить роль журналиста.
     */
    @Override
    @ServiceAspectLogger
    public void setJournalist(Long id) {
        userService.setRoleJournalist(id);
    }

    /**
     * Возвращает список всех пользователей.
     *
     * @return Список объектов {@link UserDto}, представляющих пользователей.
     */
    @Override
    @ServiceAspectLogger
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }
}
