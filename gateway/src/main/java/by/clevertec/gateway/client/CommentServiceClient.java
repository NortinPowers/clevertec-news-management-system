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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Retry(name = "comment-service-retry")
@FeignClient(name = "comment-service")
@CircuitBreaker(name = "comment-service-breaker")
public interface CommentServiceClient {

    /**
     * Получает список комментариев с пагинацией.
     *
     * @param pageable Параметры пагинации.
     * @return Ответ со страницей объектов {@link CommentResponseDto}, представляющих комментарии.
     */
    @GetMapping
    ResponseEntity<Page<CommentResponseDto>> getAll(Pageable pageable);

    /**
     * Получает данные о комментарии по его идентификатору.
     *
     * @param id Идентификатор комментария.
     * @return Ответ с данными о комментарии.
     */
    @GetMapping("/{id}")
    ResponseEntity<CommentResponseDto> getById(@PathVariable Long id);

    /**
     * Сохраняет комментарий.
     *
     * @param comment Данные о комментарии.
     * @return Ответ с сообщением об успешном сохранении комментария.
     */
    @PostMapping
    ResponseEntity<BaseResponse> save(@RequestBody CommentRequestDto comment);

    /**
     * Обновляет комментарий по его идентификатору.
     *
     * @param id      Идентификатор комментария.
     * @param comment Данные о комментарии.
     * @return Ответ с сообщением об успешном обновлении комментария.
     */
    @PutMapping("/{id}")
    ResponseEntity<BaseResponse> update(@PathVariable Long id,
                                        @RequestBody CommentRequestDto comment);

    /**
     * Обновляет комментарий по его идентификатору (path).
     *
     * @param id      Идентификатор комментария.
     * @param comment Данные о комментарии.
     * @return Ответ с сообщением об успешном обновлении комментария.
     */
    @PostMapping("/{id}")
    ResponseEntity<BaseResponse> updatePath(@PathVariable Long id,
                                            @RequestBody CommentPathRequestDto comment);

    /**
     * Удаляет комментарий по его идентификатору.
     *
     * @param id Идентификатор комментария.
     * @return Ответ с сообщением об успешном удалении комментария.
     */
    @DeleteMapping("/{id}")
    ResponseEntity<BaseResponse> delete(@PathVariable Long id,
                                        @RequestBody String username);

    /**
     * Получает комментарии к новости по её идентификатору.
     *
     * @param newsId   Идентификатор новости.
     * @param pageable Параметры пагинации.
     * @return Ответ со страницей объектов {@link CommentResponseDto}, представляющих комментарии.
     */
    @GetMapping("/news/{newsId}")
    ResponseEntity<Page<CommentResponseDto>> getAllByNewsId(@PathVariable Long newsId, Pageable pageable);

    /**
     * Получает результаты поиска комментариев по имени автора или тексту.
     *
     * @param condition Условие поиска (имя автора или текст комментария).
     * @param pageable  Параметры пагинации.
     * @return Ответ со страницей объектов {@link CommentResponseDto}, представляющих комментарии.
     */
    @GetMapping("/search/{condition}")
    ResponseEntity<Page<CommentResponseDto>> getCommentsSearchResult(@PathVariable String condition,
                                                                     Pageable pageable);
}
