package by.clevertec.gateway.client;

import by.clevertec.auth.JwtRequest;
import by.clevertec.auth.JwtResponse;
import by.clevertec.auth.UserRegistrationDto;
import by.clevertec.message.BaseResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Retry(name = "auth-service-retry")
@FeignClient(name = "auth-service")
@CircuitBreaker(name = "auth-service-breaker")
public interface AuthServiceClient {

    /**
     * Создает JWT-токен на основе данных из запроса аутентификации.
     *
     * @param request Запрос с данными о пользователе (имя пользователя и пароль).
     * @return Ответ со сгенерированным JWT-токеном.
     */
    @PostMapping
    ResponseEntity<JwtResponse> createAuthToken(@RequestBody @Valid JwtRequest request);

    /**
     * Создает нового пользователя на основе данных о регистрации.
     *
     * @param userRegistrationDto Данные о регистрации пользователя.
     * @return Ответ с сообщением об успешном создании пользователя.
     */
    @PostMapping("/registration")
    ResponseEntity<BaseResponse> createNewUser(@RequestBody @Valid UserRegistrationDto userRegistrationDto);
}
