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

@Retry(name = "news-service-retry")
@FeignClient(name = "news-service")
@CircuitBreaker(name = "news-service-breaker")
public interface NewsServiceClient {

    /**
     * Получает список новостей с пагинацией.
     *
     * @param pageable Параметры пагинации.
     * @return Ответ со страницей объектов {@link NewsResponseDto}, представляющих новости.
     */
    @GetMapping
    ResponseEntity<Page<NewsResponseDto>> getAll(Pageable pageable);

    /**
     * Получает данные о новости по её идентификатору.
     *
     * @param id Идентификатор новости.
     * @return Ответ с данными о новости.
     */
    @GetMapping("/{id}")
    ResponseEntity<NewsResponseDto> getById(@PathVariable Long id);

    /**
     * Сохраняет новость.
     *
     * @param requestDto Данные о новости и имени пользователя.
     * @return Ответ с сообщением об успешном сохранении новости.
     */
    @PostMapping
    ResponseEntity<BaseResponse> save(@RequestBody NewsAndNameRequestDto requestDto);

    /**
     * Обновляет новость по её идентификатору.
     *
     * @param id         Идентификатор новости.
     * @param requestDto Данные о новости и имени пользователя.
     * @return Ответ с сообщением об успешном обновлении новости.
     */
    @PutMapping("/{id}")
    ResponseEntity<BaseResponse> update(@PathVariable Long id,
                                        @RequestBody NewsAndNameRequestDto requestDto);

    /**
     * Обновляет новость по её идентификатору (path).
     *
     * @param id         Идентификатор новости.
     * @param requestDto Данные о новости и имени пользователя.
     * @return Ответ с сообщением об успешном обновлении новости.
     */
    @PostMapping("/{id}")
    ResponseEntity<BaseResponse> updatePath(@PathVariable Long id,
                                            @RequestBody NewsAndNamePathRequestDto requestDto);

    /**
     * Удаляет новость по её идентификатору.
     *
     * @param id       Идентификатор новости.
     * @param username Имя пользователя, выполняющего удаление.
     * @return Ответ с сообщением об успешном удалении новости.
     */
    @DeleteMapping("/{id}")
    ResponseEntity<BaseResponse> delete(@PathVariable Long id,
                                        @RequestBody String username);

    /**
     * Получает результаты поиска новостей по имени автора или заголовку.
     *
     * @param condition Условие поиска (имя автора или заголовок новости).
     * @param pageable  Параметры пагинации.
     * @return Ответ со страницей объектов {@link NewsResponseDto}, представляющих новости.
     */
    @GetMapping("/search/{condition}")
    ResponseEntity<Page<NewsResponseDto>> getPersonSearchResult(@PathVariable String condition,
                                                                Pageable pageable);
}
