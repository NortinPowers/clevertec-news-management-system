package by.clevertec.news.service;

import by.clevertec.news.domain.News;
import by.clevertec.request.NewsAndNamePathRequestDto;
import by.clevertec.request.NewsAndNameRequestDto;
import by.clevertec.request.NewsPathRequestDto;
import by.clevertec.request.NewsRequestDto;
import by.clevertec.response.NewsResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NewsService {

    /**
     * Получает список объектов {@link NewsResponseDto} с учетом параметров страницы и размера страницы.
     *
     * @param pageable Объект Pageable.
     * @return Список объектов {@link NewsResponseDto} для текущей страницы и размера страницы обернутых в Page.
     */
    Page<NewsResponseDto> getAll(Pageable pageable);

    /**
     * Получает объект {@link NewsResponseDto} по уникальному идентификатору (UUID).
     *
     * @param id Уникальный идентификатор (Long) объекта {@link NewsResponseDto}.
     * @return Объект {@link NewsResponseDto}, соответствующий указанному идентификатору.
     */
    NewsResponseDto getById(Long id);

    /**
     * Сохраняет объект {@link NewsRequestDto} из объекта {@link NewsAndNameRequestDto} в виде нового объекта {@link News}.
     *
     * @param newsDtoWithName Объект {@link NewsAndNameRequestDto}, содержащий объект который требуется сохранить и имя пользователя.
     */
    Long save(NewsAndNameRequestDto newsDtoWithName);
//    void save(NewsAndNameRequestDto newsDtoWithName);
//    News save(NewsAndNameRequestDto newsDtoWithName);

    /**
     * Обновляет данные объекта {@link News} по уникальному идентификатору (UUID).
     *
     * @param id   Уникальный идентификатор (Long) объекта {@link News}, который требуется обновить.
     * @param newsDtoWithName Объект {@link NewsAndNameRequestDto}, содержащий обновленные данные и имя пользователя.
     */
    void update(Long id, NewsAndNameRequestDto newsDtoWithName);
//    void update(Long id, NewsRequestDto newsDto);

    /**
     * Обновляет данные объекта {@link News} по уникальному идентификатору (UUID) с использованием
     * данных из объекта {@link NewsAndNamePathRequestDto}.
     *
     * @param id      Уникальный идентификатор (Long) объекта {@link News}, который требуется обновить.
     * @param newsDtoWithName Объект {@link NewsAndNamePathRequestDto}, содержащий обновленные данные и имя пользователя.
     */
    void updatePath(Long id, NewsAndNamePathRequestDto newsDtoWithName);

    /**
     * Удаляет объект {@link News} по уникальному идентификатору (UUID).
     *
     * @param id Уникальный идентификатор (Long) объекта {@link News}, который требуется удалить.
     * @param username Имя пользователя, который запрашивает метод.
     */
    void delete(Long id, String username);
//    void delete(Long id);

    Page<NewsResponseDto> findNewsSearchResult(String condition, Pageable pageable);
}
