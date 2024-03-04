package by.clevertec.gateway.utils;

import by.clevertec.gateway.projection.impl.NewsWithCommentsProjectionImpl;
import by.clevertec.gateway.security.CustomUserDetails;
import by.clevertec.request.NewsAndNamePathRequestDto;
import by.clevertec.request.NewsAndNameRequestDto;
import by.clevertec.request.NewsPathRequestDto;
import by.clevertec.request.NewsRequestDto;
import by.clevertec.response.CommentResponseDto;
import by.clevertec.response.NewsResponseDto;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

@UtilityClass
public class ControllerUtils {

    public static NewsAndNameRequestDto getNewsAndNameRequestDtoToCreate(NewsRequestDto news) {
        NewsAndNameRequestDto requestDto = new NewsAndNameRequestDto();
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        requestDto.setName(username);
        requestDto.setRequestDto(news);
        return requestDto;
    }

    public static NewsAndNameRequestDto getNewsAndNameRequestDtoToUpdate(NewsRequestDto news) {
        NewsAndNameRequestDto requestDto = new NewsAndNameRequestDto();
        String username = getUsername();
        requestDto.setName(username);
        requestDto.setRequestDto(news);
        return requestDto;
    }

    public static NewsAndNamePathRequestDto getNewsAndNamePathRequestDtoToUpdate(NewsPathRequestDto news) {
        NewsAndNamePathRequestDto requestDto = new NewsAndNamePathRequestDto();
        String username = getUsername();
        requestDto.setName(username);
        requestDto.setRequestDto(news);
        return requestDto;
    }

    public static String getUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        return authorities.stream()
                .filter(grantedAuthority -> grantedAuthority.getAuthority().equals("ADMIN"))
                .findAny()
                .map(grantedAuthority -> "!ADMIN")
                .orElseGet(authentication::getName);
    }

    public static NewsWithCommentsProjectionImpl getNewsWithCommentsProjection(NewsResponseDto newsResponseDto, Page<CommentResponseDto> commentResponseDtos) {
        return new NewsWithCommentsProjectionImpl(
                LocalDateTime.parse(newsResponseDto.getTime(), DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                newsResponseDto.getTitle(),
                newsResponseDto.getText(),
                newsResponseDto.getAuthor(),
                commentResponseDtos);
    }

    public static CustomUserDetails getUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String username = authentication.getName();
//        String username = (String) authentication.getPrincipal();
        return new CustomUserDetails(username, authorities);
    }
}
