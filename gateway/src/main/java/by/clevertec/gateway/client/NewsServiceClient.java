package by.clevertec.gateway.client;

import by.clevertec.message.BaseResponse;
import by.clevertec.request.NewsAndNamePathRequestDto;
import by.clevertec.request.NewsAndNameRequestDto;
import by.clevertec.response.NewsResponseDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "news-service")
@CircuitBreaker(name = "news-service-breaker")
@Retry(name = "news-service-retry")
public interface NewsServiceClient {

    @GetMapping
    ResponseEntity<Page<NewsResponseDto>> getAll(Pageable pageable);

    @GetMapping("/{id}")
    ResponseEntity<NewsResponseDto> getById(@PathVariable Long id);

    @PostMapping
    ResponseEntity<BaseResponse> save(@RequestBody NewsAndNameRequestDto requestDto);

    @PutMapping("/{id}")
    ResponseEntity<BaseResponse> update(@PathVariable Long id,
                                        @RequestBody NewsAndNameRequestDto requestDto);

    @PostMapping("/{id}")
    ResponseEntity<BaseResponse> updatePath(@PathVariable Long id,
                                            @RequestBody NewsAndNamePathRequestDto requestDto);

    @DeleteMapping("/{id}")
    ResponseEntity<BaseResponse> delete(@PathVariable Long id,
                                        @RequestBody String username);

    @GetMapping("/search/{condition}")
    ResponseEntity<Page<NewsResponseDto>> getPersonSearchResult(@PathVariable String condition,
                                                                Pageable pageable);
}
