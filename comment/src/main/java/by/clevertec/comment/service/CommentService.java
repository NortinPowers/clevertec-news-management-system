package by.clevertec.comment.service;

import by.clevertec.comment.domain.Comment;
import by.clevertec.request.CommentAndNamePathRequestDto;
import by.clevertec.request.CommentAndNameRequestDto;
import by.clevertec.request.CommentRequestDto;
import by.clevertec.response.CommentResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentService {

    /**
     * Получает список объектов {@link CommentResponseDto} с учетом параметров страницы и размера страницы.
     *
     * @param pageable Объект Pageable.
     * @return Список объектов {@link CommentResponseDto} для текущей страницы и размера страницы обернутых в Page.
     */
    Page<CommentResponseDto> getAll(Pageable pageable);

    /**
     * Получает объект {@link CommentResponseDto} по уникальному идентификатору (UUID).
     *
     * @param id Уникальный идентификатор (Long) объекта {@link CommentResponseDto}.
     * @return Объект {@link CommentResponseDto}, соответствующий указанному идентификатору.
     */
    CommentResponseDto getById(Long id);

    /**
     * Сохраняет объект {@link CommentRequestDto} из объекта {@link CommentAndNameRequestDto} в виде нового объекта {@link Comment}.
     *
     * @param commentDtoWithName Объект {@link CommentAndNameRequestDto}, который требуется сохранить в виде нового пользователя.
     */
    Long save(CommentAndNameRequestDto commentDtoWithName);
//    void save(CommentAndNameRequestDto commentDtoWithName);
//    Comment save(CommentAndNameRequestDto commentDtoWithName);

    /**
     * Обновляет данные объекта {@link Comment} по уникальному идентификатору (UUID).
     *
     * @param id                 Уникальный идентификатор (Long) объекта {@link Comment}, который требуется обновить.
     * @param commentDtoWithName Объект {@link CommentAndNameRequestDto}, одержащий обновленные данные и имя пользователя.
     */
    void update(Long id, CommentAndNameRequestDto commentDtoWithName);

    /**
     * Обновляет данные объекта {@link Comment} по уникальному идентификатору (UUID) с использованием
     * данных из объекта {@link CommentAndNamePathRequestDto}.
     *
     * @param id                 Уникальный идентификатор (Long) объекта {@link Comment}, который требуется обновить.
     * @param commentDtoWithName Объект {@link CommentAndNamePathRequestDto}, содержащий обновленные данные и имя пользователя.
     */
    void updatePath(Long id, CommentAndNamePathRequestDto commentDtoWithName);

    /**
     * Удаляет объект {@link Comment} по уникальному идентификатору (UUID).
     *
     * @param id       Уникальный идентификатор (Long) объекта {@link Comment}, который требуется удалить.
     * @param username Имя пользователя, который запрашивает метод.
     */
    void delete(Long id, String username);

//    List<CommentResponseDto> getAllByNewsId(Long newsId);

    Page<CommentResponseDto> getAllByNewsId(Long newsId, Pageable pageable);

    Page<CommentResponseDto> findCommentsSearchResult(String condition, Pageable pageable);
}
