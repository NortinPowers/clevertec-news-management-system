package by.clevertec.gateway.utils;

import by.clevertec.gateway.projection.impl.NewsWithCommentsProjectionImpl;
import by.clevertec.gateway.security.CustomUserDetails;
import by.clevertec.request.NewsAndNamePathRequestDto;
import by.clevertec.request.NewsAndNameRequestDto;
import by.clevertec.request.NewsPathRequestDto;
import by.clevertec.request.NewsRequestDto;
import by.clevertec.response.CommentResponseDto;
import by.clevertec.response.NewsResponseDto;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

@UtilityClass
public class ControllerUtils {

    /**
     * Создает объект {@link NewsAndNameRequestDto} для создания новости.
     *
     * @param news Данные о новости.
     * @return Объект {@link NewsAndNameRequestDto} с именем пользователя и данными о новости.
     */
    public static NewsAndNameRequestDto getNewsAndNameRequestDtoToCreate(NewsRequestDto news) {
        NewsAndNameRequestDto requestDto = new NewsAndNameRequestDto();
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        requestDto.setName(username);
        requestDto.setRequestDto(news);
        return requestDto;
    }

    /**
     * Создает объект {@link NewsAndNameRequestDto} для обновления новости.
     *
     * @param news Данные о новости.
     * @return Объект {@link NewsAndNameRequestDto} с именем пользователя и данными о новости.
     */
    public static NewsAndNameRequestDto getNewsAndNameRequestDtoToUpdate(NewsRequestDto news) {
        NewsAndNameRequestDto requestDto = new NewsAndNameRequestDto();
        String username = getUsername();
        requestDto.setName(username);
        requestDto.setRequestDto(news);
        return requestDto;
    }

    /**
     * Создает объект {@link NewsAndNamePathRequestDto} для обновления новости (path).
     *
     * @param news Данные о новости.
     * @return Объект {@link NewsAndNamePathRequestDto} с именем пользователя и данными о новости.
     */
    public static NewsAndNamePathRequestDto getNewsAndNamePathRequestDtoToUpdate(NewsPathRequestDto news) {
        NewsAndNamePathRequestDto requestDto = new NewsAndNamePathRequestDto();
        String username = getUsername();
        requestDto.setName(username);
        requestDto.setRequestDto(news);
        return requestDto;
    }

    /**
     * Получает имя пользователя из контекста безопасности.
     * Если пользователь администратор, устанавливает имя суперпользователя.
     *
     * @return Имя пользователя.
     */
    public static String getUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        return authorities.stream()
                .filter(grantedAuthority -> grantedAuthority.getAuthority().equals("ADMIN"))
                .findAny()
                .map(grantedAuthority -> "!ADMIN")
                .orElseGet(authentication::getName);
    }

    /**
     * Создает объект {@link NewsWithCommentsProjectionImpl} на основе данных о новости и комментариях.
     *
     * @param newsResponseDto     Данные о новости.
     * @param commentResponseDtos Страница комментариев.
     * @return Объект {@link NewsWithCommentsProjectionImpl}, объединяющий новость и комментарии.
     */
    public static NewsWithCommentsProjectionImpl getNewsWithCommentsProjection(NewsResponseDto newsResponseDto, Page<CommentResponseDto> commentResponseDtos) {
        return new NewsWithCommentsProjectionImpl(
                LocalDateTime.parse(newsResponseDto.getTime(), DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                newsResponseDto.getTitle(),
                newsResponseDto.getText(),
                newsResponseDto.getAuthor(),
                commentResponseDtos);
    }

    /**
     * Получает детали пользователя из контекста безопасности.
     *
     * @return Объект {@link CustomUserDetails}, содержащий имя пользователя и его роли.
     */
    public static CustomUserDetails getUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String username = authentication.getName();
        return new CustomUserDetails(username, authorities);
    }
}
