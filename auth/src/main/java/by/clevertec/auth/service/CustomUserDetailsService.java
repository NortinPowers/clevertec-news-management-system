package by.clevertec.auth.service;

import by.clevertec.aspect.ServiceAspectLogger;
import by.clevertec.auth.domain.User;
import by.clevertec.auth.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;

    /**
     * Загружает данные о пользователе по его имени пользователя.
     *
     * @param username Имя пользователя для поиска.
     * @return Детали пользователя, упакованные в объект {@link CustomUserDetails}.
     * @throws UsernameNotFoundException Если пользователь с указанным именем не найден.
     */
    @Override
    @ServiceAspectLogger
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.getUserByUsername(username);
        return new CustomUserDetails(user);
    }
}
