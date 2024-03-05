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

@CircuitBreaker(name = "admin-service-breaker")
@Retry(name = "admin-service-retry")
@FeignClient(name = "admin-service")
public interface AdminServiceClient {

    @PostMapping("/set/admin/{id}")
    ResponseEntity<BaseResponse> setAdmin(@PathVariable("id") @Min(1) Long id,
                                          @RequestHeader("Authorization") String header);

    @PostMapping("/set/journalist/{id}")
    ResponseEntity<BaseResponse> setJournalist(@PathVariable("id") @Min(1) Long id,
                                               @RequestHeader("Authorization") String header);

    @GetMapping("/users")
    ResponseEntity<List<UserDto>> getAllUsers(@RequestHeader("Authorization") String header);
}
