package by.clevertec.auth.service;

import by.clevertec.auth.UserDto;
import java.util.List;

public interface AdminService {

    /**
     * Назначает роль администратора пользователю с указанным идентификатором.
     *
     * @param id Идентификатор пользователя, которому нужно назначить роль администратора.
     */
    void setAdmin(Long id);

    /**
     * Назначает роль журналиста пользователю с указанным идентификатором.
     *
     * @param id Идентификатор пользователя, которому нужно назначить роль журналиста.
     */
    void setJournalist(Long id);

    /**
     * Возвращает список всех пользователей.
     *
     * @return Список объектов {@link UserDto}, представляющих пользователей.
     */
    List<UserDto> getAllUsers();
}
