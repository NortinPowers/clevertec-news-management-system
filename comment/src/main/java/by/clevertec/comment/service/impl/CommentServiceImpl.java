package by.clevertec.comment.service.impl;

import static by.clevertec.util.CheckerUtil.checkList;

import by.clevertec.aspect.ServiceAspectLogger;
import by.clevertec.comment.domain.Author;
import by.clevertec.comment.domain.Comment;
import by.clevertec.comment.mapper.CommentMapper;
import by.clevertec.comment.proxy.CommentCacheable;
import by.clevertec.comment.repository.CommentRepository;
import by.clevertec.comment.service.AuthorService;
import by.clevertec.comment.service.CommentService;
import by.clevertec.exception.CustomAccessException;
import by.clevertec.exception.CustomEntityNotFoundException;
import by.clevertec.request.CommentAndNamePathRequestDto;
import by.clevertec.request.CommentAndNameRequestDto;
import by.clevertec.request.CommentRequestDto;
import by.clevertec.response.CommentResponseDto;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService {

    private final CommentRepository repository;
    private final CommentMapper mapper;
    private final AuthorService authorService;

    /**
     * Получает страницу объектов {@link CommentResponseDto} с помощью пагинации.
     *
     * @param pageable Объект, представляющий параметры пагинации.
     * @return Страница с объектами {@link CommentResponseDto}.
     */
    @Override
    @ServiceAspectLogger
    public Page<CommentResponseDto> getAll(Pageable pageable) {
        Page<CommentResponseDto> commentsPage = repository.findAll(pageable)
                .map(mapper::toDto);
        checkList(commentsPage.getContent(), Comment.class);
        return commentsPage;
    }

    /**
     * Получает объект {@link CommentResponseDto} по уникальному идентификатору.
     *
     * @param id Идентификатор объекта {@link Comment}.
     * @return Объект {@link CommentResponseDto} с указанным идентификатором.
     * @throws CustomEntityNotFoundException Если объект с указанным идентификатором не найден.
     */
    @Override
    @Transactional
    @ServiceAspectLogger
    @Cacheable(value = "CommentService::getById", key = "#id")
    public CommentResponseDto getById(Long id) {
        return repository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> CustomEntityNotFoundException.of(Comment.class, id));
    }

    /**
     * Сохраняет объект {@link Comment} и возвращает его уникальный идентификатор.
     *
     * @param commentDtoWithName DTO с данными для создания объекта {@link Comment} и имени автора.
     * @return Уникальный идентификатор сохраненного объекта {@link Comment}.
     */
    @Override
    @Transactional
    @CommentCacheable
    @ServiceAspectLogger
    public Long save(CommentAndNameRequestDto commentDtoWithName) {
        CommentRequestDto dto = commentDtoWithName.getRequestDto();
        Comment comment = mapper.toDomain(dto);
        Author author = authorService.getByName(commentDtoWithName.getName());
        comment.setAuthor(author);
        return repository.save(comment).getId();
    }

    /**
     * Обновляет объект {@link Comment} по указанному идентификатору.
     *
     * @param id                 Идентификатор объекта {@link Comment}, который будет обновлен.
     * @param commentDtoWithName DTO с обновленными данными для объекта {@link Comment} и именем автора.
     * @throws CustomAccessException         Если доступ к обновлению запрещен.
     * @throws CustomEntityNotFoundException Если объект с указанным идентификатором не найден.
     */
    @Override
    @Transactional
    @ServiceAspectLogger
    @CachePut(value = "CommentService::getById", key = "#id")
    public void update(Long id, CommentAndNameRequestDto commentDtoWithName) {
        Optional<Comment> commentOptional = repository.findById(id);
        if (commentOptional.isPresent()) {
            Comment comment = commentOptional.get();
            if (hasAccess(commentDtoWithName, comment)) {
                Comment updated = mapper.toDomain(commentDtoWithName.getRequestDto());
                if (!comment.equals(updated)) {
                    Comment merged = mapper.merge(comment, updated);
                    merged.setTime(comment.getTime());
                }
            } else {
                throw CustomAccessException.of(Comment.class);
            }
        } else {
            throw CustomEntityNotFoundException.of(Comment.class, id);
        }
    }

    /**
     * Обновляет объект {@link Comment} по указанному идентификатору.
     *
     * @param id                 Идентификатор объекта {@link Comment}, который будет обновлен.
     * @param commentDtoWithName DTO с обновленными данными для объекта {@link Comment} и именем автора.
     * @throws CustomAccessException         Если доступ к обновлению запрещен.
     * @throws CustomEntityNotFoundException Если объект с указанным идентификатором не найден.
     */
    @Override
    @ServiceAspectLogger
    @Transactional
    @CachePut(value = "CommentService::getById", key = "#id")
    public void updatePath(Long id, CommentAndNamePathRequestDto commentDtoWithName) {
        Optional<Comment> commentOptional = repository.findById(id);
        if (commentOptional.isPresent()) {
            Comment comment = commentOptional.get();
            if (hasAccess(commentDtoWithName, comment)) {
                Comment updated = mapper.toDomain(commentDtoWithName.getRequestDto());
                if (!comment.equals(updated)) {
                    Comment merged = mapper.merge(comment, updated);
                    merged.setTime(comment.getTime());
                }
            } else {
                throw CustomAccessException.of(Comment.class);
            }
        } else {
            throw CustomEntityNotFoundException.of(Comment.class, id);
        }
    }

    /**
     * Удаляет объект {@link Comment} по указанному идентификатору.
     *
     * @param id       Идентификатор объекта {@link Comment}, который будет удален.
     * @param username Имя пользователя, выполняющего операцию удаления.
     * @throws CustomAccessException         Если доступ к удалению запрещен.
     * @throws CustomEntityNotFoundException Если объект с указанным идентификатором не найден.
     */
    @Override
    @Transactional
    @ServiceAspectLogger
    @CacheEvict(value = "CommentService::getById", key = "#id")
    public void delete(Long id, String username) {
        Optional<Comment> commentOptional = repository.findById(id);
        if (commentOptional.isPresent()) {
            Comment comment = commentOptional.get();
            if (hasAccess(username, comment)) {
                repository.deleteById(id);
            } else {
                throw CustomAccessException.of(Comment.class);
            }
        } else {
            throw CustomEntityNotFoundException.of(Comment.class, id);
        }
    }

    /**
     * Получает страницу объектов {@link CommentResponseDto}, связанных с новостью по указанному идентификатору.
     *
     * @param newsId   Идентификатор новости.
     * @param pageable Объект, представляющий параметры пагинации.
     * @return Страница с объектами {@link CommentResponseDto}, связанными с указанной новостью.
     */
    @Override
    @ServiceAspectLogger
    public Page<CommentResponseDto> getAllByNewsId(Long newsId, Pageable pageable) {
        Page<CommentResponseDto> commentsPage = repository.findByNewsId(newsId, pageable)
                .map(mapper::toDto);
        checkList(commentsPage.getContent(), Comment.class);
        return commentsPage;
    }

    /**
     * Ищет объекты {@link CommentResponseDto} по заданному условию с использованием пагинации.
     *
     * @param condition Условие поиска (например, ключевое слово).
     * @param pageable  Объект, представляющий параметры пагинации.
     * @return Страница с объектами {@link CommentResponseDto}, удовлетворяющими условию поиска.
     */
    @Override
    @ServiceAspectLogger
    public Page<CommentResponseDto> findCommentsSearchResult(String condition, Pageable pageable) {
        Page<CommentResponseDto> commentsPage = repository.getCommentsSearchResult(condition, pageable)
                .map(mapper::toDto);
        checkList(commentsPage.getContent(), Comment.class);
        return commentsPage;
    }

    /**
     * Проверяет доступ к объекту {@link Comment} на основе имени пользователя и объекта новости.
     *
     * @param t       Объект, представляющий имя пользователя или DTO с именем.
     * @param comment Объект {@link Comment}, к которому проверяется доступ.
     * @param <T>     Тип объекта (String или DTO).
     * @return true, если доступ разрешен, иначе false.
     */
    private <T> boolean hasAccess(T t, Comment comment) {
        String name;
        if (t instanceof CommentAndNameRequestDto requestDto) {
            name = requestDto.getName();
        } else if (t instanceof CommentAndNamePathRequestDto requestDto) {
            name = requestDto.getName();
        } else {
            name = (String) t;
        }
        return name.equals(comment.getAuthor().getName()) || "!ADMIN".equals(name);
    }
}
