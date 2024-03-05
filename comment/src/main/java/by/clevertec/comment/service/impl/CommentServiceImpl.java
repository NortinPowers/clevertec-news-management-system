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

    @Override
    @ServiceAspectLogger
    public Page<CommentResponseDto> getAll(Pageable pageable) {
        Page<CommentResponseDto> commentsPage = repository.findAll(pageable)
                .map(mapper::toDto);
        checkList(commentsPage.getContent(), Comment.class);
        return commentsPage;
    }

    @Override
//    @CustomCacheable
    @ServiceAspectLogger
    @Transactional
    @Cacheable(value = "CommentService::getById", key = "#id")
    public CommentResponseDto getById(Long id) {
        return repository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> CustomEntityNotFoundException.of(Comment.class, id));
    }

    @Override
    @CommentCacheable
    @ServiceAspectLogger
    @Transactional
//    @Cacheable(value = "CommentService::getById", key = "#result.id")
    public Long save(CommentAndNameRequestDto commentDtoWithName) {
//    public void save(CommentAndNameRequestDto commentDtoWithName) {
//    public Comment save(CommentAndNameRequestDto commentDtoWithName) {
        CommentRequestDto dto = commentDtoWithName.getRequestDto();
        Comment comment = mapper.toDomain(dto);
        Author author = authorService.getByName(commentDtoWithName.getName());
        comment.setAuthor(author);
        return repository.save(comment).getId();
//       repository.save(comment);
//        return repository.save(comment);
    }

    @Override
//    @CustomCacheable
    @ServiceAspectLogger
    @Transactional
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

    //TODO
    @Override
//    @CustomCacheable
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

    @Override
//    @CustomCacheable
    @ServiceAspectLogger
    @Transactional
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

//    @Override
//    public List<CommentResponseDto> getAllByNewsId(Long newsId) {
//        List<Comment> commentsByNewsId = repository.findAllByNewsId(newsId);
//        return commentsByNewsId.stream()
//                .map(mapper::toDto)
//                .toList();
//    }

    @Override
    @ServiceAspectLogger
    public Page<CommentResponseDto> getAllByNewsId(Long newsId, Pageable pageable) {
        Page<CommentResponseDto> commentsPage = repository.findByNewsId(newsId, pageable)
                .map(mapper::toDto);
        checkList(commentsPage.getContent(), Comment.class);
        return commentsPage;
    }

    @Override
    @ServiceAspectLogger
    public Page<CommentResponseDto> findCommentsSearchResult(String condition, Pageable pageable) {
        Page<CommentResponseDto> commentsPage = repository.getCommentsSearchResult(condition, pageable)
                .map(mapper::toDto);
        checkList(commentsPage.getContent(), Comment.class);
        return commentsPage;
    }

    private <T> boolean hasAccess(T t, Comment comment) {
        String name;
        if (t instanceof CommentAndNameRequestDto requestDto) {
            name = requestDto.getName();
        } else if (t instanceof CommentAndNamePathRequestDto requestDto) {
            name = requestDto.getName();
        } else {
            name = (String) t;
        }
        return name.equals(comment.getAuthor().getName()) || ("!ADMIN").equals(name);
    }
}
