package by.clevertec.gateway.client;

import by.clevertec.auth.UserDto;
import by.clevertec.message.BaseResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.validation.constraints.Min;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "admin-service")
@Retry(name = "admin-service-retry")
@CircuitBreaker(name = "admin-service-breaker")
public interface AdminServiceClient {

    /**
     * Назначает роль администратора пользователю с указанным идентификатором.
     *
     * @param id     Идентификатор пользователя, которому нужно назначить роль администратора.
     * @param header Заголовок авторизации (токен).
     * @return Ответ с сообщением об успешном изменении роли пользователя.
     */
    @PostMapping("/set/admin/{id}")
    ResponseEntity<BaseResponse> setAdmin(@PathVariable("id") @Min(1) Long id,
                                          @RequestHeader("Authorization") String header);

    /**
     * Назначает роль журналиста пользователю с указанным идентификатором.
     *
     * @param id     Идентификатор пользователя, которому нужно назначить роль журналиста.
     * @param header Заголовок авторизации (токен).
     * @return Ответ с сообщением об успешном изменении роли пользователя.
     */
    @PostMapping("/set/journalist/{id}")
    ResponseEntity<BaseResponse> setJournalist(@PathVariable("id") @Min(1) Long id,
                                               @RequestHeader("Authorization") String header);

    /**
     * Возвращает список всех пользователей.
     *
     * @param header Заголовок авторизации (токен).
     * @return Ответ со списком объектов {@link UserDto}, представляющих пользователей.
     */
    @GetMapping("/users")
    ResponseEntity<List<UserDto>> getAllUsers(@RequestHeader("Authorization") String header);
}
