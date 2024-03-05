package by.clevertec.news.service.impl;

import static by.clevertec.util.CheckerUtil.checkList;

import by.clevertec.aspect.ServiceAspectLogger;
import by.clevertec.exception.CustomAccessException;
import by.clevertec.exception.CustomEntityNotFoundException;
import by.clevertec.news.domain.Author;
import by.clevertec.news.domain.News;
import by.clevertec.news.mapper.NewsMapper;
import by.clevertec.news.proxy.NewsCacheable;
import by.clevertec.news.repository.NewsRepository;
import by.clevertec.news.service.AuthorService;
import by.clevertec.news.service.NewsService;
import by.clevertec.request.NewsAndNamePathRequestDto;
import by.clevertec.request.NewsAndNameRequestDto;
import by.clevertec.request.NewsRequestDto;
import by.clevertec.response.NewsResponseDto;
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
public class NewsServiceImpl implements NewsService {

    private final NewsRepository repository;
    private final NewsMapper mapper;
    private final AuthorService authorService;

    /**
     * Получает страницу объектов {@link NewsResponseDto} с помощью пагинации.
     *
     * @param pageable Объект, представляющий параметры пагинации.
     * @return Страница с объектами {@link NewsResponseDto}.
     */
    @Override
    @ServiceAspectLogger
    public Page<NewsResponseDto> getAll(Pageable pageable) {
        Page<NewsResponseDto> newsPage = repository.findAll(pageable)
                .map(mapper::toDto);
        checkList(newsPage.getContent(), News.class);
        return newsPage;
    }

    /**
     * Получает объект {@link NewsResponseDto} по уникальному идентификатору.
     *
     * @param id Идентификатор объекта {@link News}.
     * @return Объект {@link NewsResponseDto} с указанным идентификатором.
     * @throws CustomEntityNotFoundException Если объект с указанным идентификатором не найден.
     */
    @Override
    @ServiceAspectLogger
    @Cacheable(value = "NewsService::getById", key = "#id")
    public NewsResponseDto getById(Long id) {
        return repository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> CustomEntityNotFoundException.of(News.class, id));
    }

    /**
     * Сохраняет объект {@link News} и возвращает его уникальный идентификатор.
     *
     * @param newsDtoWithName DTO с данными для создания объекта {@link News} и имени автора.
     * @return Уникальный идентификатор сохраненного объекта {@link News}.
     */
    @Override
    @Transactional
    @NewsCacheable
    @ServiceAspectLogger
    public Long save(NewsAndNameRequestDto newsDtoWithName) {
        NewsRequestDto dto = newsDtoWithName.getRequestDto();
        News news = mapper.toDomain(dto);
        Author author = authorService.getByName(newsDtoWithName.getName());
        news.setAuthor(author);
        return repository.save(news).getId();
    }

    /**
     * Обновляет объект {@link News} по указанному идентификатору.
     *
     * @param id              Идентификатор объекта {@link News}, который будет обновлен.
     * @param newsDtoWithName DTO с обновленными данными для объекта {@link News} и именем автора.
     * @throws CustomAccessException         Если доступ к обновлению запрещен.
     * @throws CustomEntityNotFoundException Если объект с указанным идентификатором не найден.
     */
    @Override
    @Transactional
    @ServiceAspectLogger
    @CachePut(value = "NewsService::getById", key = "#id")
    public void update(Long id, NewsAndNameRequestDto newsDtoWithName) {
        Optional<News> newsOptional = repository.findById(id);
        if (newsOptional.isPresent()) {
            News news = newsOptional.get();
            if (hasAccess(newsDtoWithName, news)) {
                News updated = mapper.toDomain(newsDtoWithName.getRequestDto());
                if (!news.equals(updated)) {
                    News merged = mapper.merge(news, updated);
                    merged.setTime(news.getTime());
                }
            } else {
                throw CustomAccessException.of(News.class);
            }
        } else {
            throw CustomEntityNotFoundException.of(News.class, id);
        }
    }

    /**
     * Обновляет объект {@link News} по указанному идентификатору.
     *
     * @param id              Идентификатор объекта {@link News}, который будет обновлен.
     * @param newsDtoWithName DTO с обновленными данными для объекта {@link News} и именем автора.
     * @throws CustomAccessException         Если доступ к обновлению запрещен.
     * @throws CustomEntityNotFoundException Если объект с указанным идентификатором не найден.
     */
    @Override
    @Transactional
    @ServiceAspectLogger
    @CachePut(value = "NewsService::getById", key = "#id")
    public void updatePath(Long id, NewsAndNamePathRequestDto newsDtoWithName) {
        Optional<News> newsOptional = repository.findById(id);
        if (newsOptional.isPresent()) {
            News news = newsOptional.get();
            if (hasAccess(newsDtoWithName, news)) {
                News updated = mapper.toDomain(newsDtoWithName.getRequestDto());
                if (!news.equals(updated)) {
                    News merged = mapper.merge(news, updated);
                    merged.setTime(news.getTime());
                }
            } else {
                throw CustomAccessException.of(News.class);
            }
        } else {
            throw CustomEntityNotFoundException.of(News.class, id);
        }
    }

    /**
     * Удаляет объект {@link News} по указанному идентификатору.
     *
     * @param id       Идентификатор объекта {@link News}, который будет удален.
     * @param username Имя пользователя, выполняющего операцию удаления.
     * @throws CustomAccessException         Если доступ к удалению запрещен.
     * @throws CustomEntityNotFoundException Если объект с указанным идентификатором не найден.
     */
    @Override
    @ServiceAspectLogger
    @Transactional
    @CacheEvict(value = "NewsService::getById", key = "#id")
    public void delete(Long id, String username) {
        Optional<News> newsOptional = repository.findById(id);
        if (newsOptional.isPresent()) {
            News news = newsOptional.get();
            if (hasAccess(username, news)) {
                repository.deleteById(id);
            } else {
                throw CustomAccessException.of(News.class);
            }
        } else {
            throw CustomEntityNotFoundException.of(News.class, id);
        }
    }

    /**
     * Ищет объекты {@link NewsResponseDto} по заданному условию с использованием пагинации.
     *
     * @param condition Условие поиска (например, ключевое слово).
     * @param pageable  Объект, представляющий параметры пагинации.
     * @return Страница с объектами {@link NewsResponseDto}, удовлетворяющими условию поиска.
     */
    @Override
    @ServiceAspectLogger
    public Page<NewsResponseDto> findNewsSearchResult(String condition, Pageable pageable) {
        Page<NewsResponseDto> newsPage = repository.getNewsSearchResult(condition, pageable)
                .map(mapper::toDto);
        checkList(newsPage.getContent(), News.class);
        return newsPage;
    }

    /**
     * Проверяет доступ к объекту {@link News} на основе имени пользователя и объекта новости.
     *
     * @param t    Объект, представляющий имя пользователя или DTO с именем.
     * @param news Объект {@link News}, к которому проверяется доступ.
     * @param <T>  Тип объекта (String или DTO).
     * @return true, если доступ разрешен, иначе false.
     */
    private <T> boolean hasAccess(T t, News news) {
        String name;
        if (t instanceof NewsAndNameRequestDto newsDtoWithName) {
            name = newsDtoWithName.getName();
        } else if (t instanceof NewsAndNamePathRequestDto newsDtoWithName) {
            name = newsDtoWithName.getName();
        } else {
            name = (String) t;
        }
        return name.equals(news.getAuthor().getName()) || "!ADMIN".equals(name);
    }
}
