package by.clevertec.gateway.client;

import by.clevertec.message.BaseResponse;
import by.clevertec.request.CommentPathRequestDto;
import by.clevertec.request.CommentRequestDto;
import by.clevertec.response.CommentResponseDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "comment-service")
@CircuitBreaker(name = "comment-service-breaker")
@Retry(name = "comment-service-retry")
//@FeignClient(name = "comment-service", url = "http://localhost:8002/comments")
public interface CommentServiceClient {

    @GetMapping
    ResponseEntity<Page<CommentResponseDto>> getAll(Pageable pageable);

    @GetMapping("/{id}")
    ResponseEntity<CommentResponseDto> getById(@PathVariable Long id);

    @PostMapping
    ResponseEntity<BaseResponse> save(@RequestBody CommentRequestDto comment);


    @PutMapping("/{id}")
    ResponseEntity<BaseResponse> update(@PathVariable Long id,
                                        @RequestBody CommentRequestDto comment);

//    @PatchMapping("/{id}")
    @PostMapping("/{id}")
    ResponseEntity<BaseResponse> updatePath(@PathVariable Long id,
                                            @RequestBody CommentPathRequestDto comment);


    @DeleteMapping("/{id}")
    ResponseEntity<BaseResponse> delete(@PathVariable Long id);

    @GetMapping("/news/{newsId}")
    ResponseEntity<Page<CommentResponseDto>> getAllByNewsId(@PathVariable Long newsId, Pageable pageable);

    @GetMapping("/search/{condition}")
    ResponseEntity<Page<CommentResponseDto>> getCommentsSearchResult(@PathVariable String condition,
                                                                   Pageable pageable);
}
