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
//@ConditionalOnProperty(name = "caching.enabled", havingValue = "true", matchIfMissing = true)
public class NewsServiceImpl implements NewsService {

    private final NewsRepository repository;
    private final NewsMapper mapper;
    private final AuthorService authorService;

    @Override
    @ServiceAspectLogger
    public Page<NewsResponseDto> getAll(Pageable pageable) {
        Page<NewsResponseDto> newsPage = repository.findAll(pageable)
                .map(mapper::toDto);
        checkList(newsPage.getContent(), News.class);
        return newsPage;
    }

    @Override
//    @NewsCacheable
    @ServiceAspectLogger
    @Cacheable(value = "NewsService::getById", key = "#id")
    public NewsResponseDto getById(Long id) {
        return repository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> CustomEntityNotFoundException.of(News.class, id));
    }

    @Override
    @Transactional
    @NewsCacheable
    @ServiceAspectLogger
//    @Cacheable(value = "NewsService::getById", key = "#result.id")
    public Long save(NewsAndNameRequestDto newsDtoWithName) {
//    public News save(NewsAndNameRequestDto newsDtoWithName) {
        NewsRequestDto dto = newsDtoWithName.getRequestDto();
        News news = mapper.toDomain(dto);
        Author author = authorService.getByName(newsDtoWithName.getName());
        news.setAuthor(author);
        return repository.save(news).getId();
//        return repository.save(news);
    }

    //TODO потестить на null и свести к одному
    @Override
    @Transactional
//    @NewsCacheable
    @ServiceAspectLogger
//    @Caching(put = @CachePut(value = "NewsService::getById", key = "#id"))
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

    @Override
    @Transactional
//    @NewsCacheable
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

    @Override
//    @NewsCacheable
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

    @Override
    @ServiceAspectLogger
    public Page<NewsResponseDto> findNewsSearchResult(String condition, Pageable pageable) {
        Page<NewsResponseDto> newsPage = repository.getNewsSearchResult(condition, pageable)
                .map(mapper::toDto);
        checkList(newsPage.getContent(), News.class);
        return newsPage;
    }

//    @Override
//    public Page<NewsWithCommentsProjection> findAllProjectedBy(Pageable pageable) {
//        return repository.findAllProjectedBy(pageable);
//    }

    private <T> boolean hasAccess(T t, News news) {
        String name;
        if (t instanceof NewsAndNameRequestDto newsDtoWithName) {
            name = newsDtoWithName.getName();
        } else if (t instanceof NewsAndNamePathRequestDto newsDtoWithName) {
            name = newsDtoWithName.getName();
        } else {
            name = (String) t;
        }
        return name.equals(news.getAuthor().getName()) || ("!ADMIN").equals(name);
    }
}
