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

@FeignClient(name = "auth-service")
@CircuitBreaker(name = "auth-service-breaker")
@Retry(name = "auth-service-retry")
public interface AuthServiceClient {

    @PostMapping
    ResponseEntity<JwtResponse> createAuthToken(@RequestBody @Valid JwtRequest request);

    @PostMapping("/registration")
    ResponseEntity<BaseResponse> createNewUser(@RequestBody @Valid UserRegistrationDto userRegistrationDto);
}
