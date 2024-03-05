package by.clevertec.auth.service;

import by.clevertec.auth.UserDto;
import by.clevertec.auth.domain.User;
import by.clevertec.auth.dto.UserRegistrationDto;
import java.util.List;

public interface UserService {

    /**
     * Получает пользователя по его имени пользователя.
     *
     * @param username Имя пользователя для поиска.
     * @return Пользователь с указанным именем пользователя или {@code null}, если пользователь не найден.
     */
    User getUserByUsername(String username);

    /**
     * Сохраняет данные о регистрации пользователя.
     *
     * @param userRegistrationDto Данные о регистрации пользователя для сохранения.
     */
    void save(UserRegistrationDto userRegistrationDto);

    /**
     * Проверяет, существует ли пользователь с указанным именем пользователя.
     *
     * @param username Имя пользователя для проверки.
     * @return {@code true}, если пользователь существует, в противном случае {@code false}.
     */
    boolean isUserExist(String username);

    /**
     * Назначает роль администратора пользователю с указанным идентификатором.
     *
     * @param id Идентификатор пользователя, которому нужно назначить роль администратора.
     */
    void setRoleAdmin(Long id);

    /**
     * Назначает роль журналиста пользователю с указанным идентификатором.
     *
     * @param id Идентификатор пользователя, которому нужно назначить роль журналиста.
     */
    void setRoleJournalist(Long id);

    /**
     * Возвращает список всех пользователей.
     *
     * @return Список объектов {@link UserDto}, представляющих пользователей.
     */
    List<UserDto> getAllUsers();
}
