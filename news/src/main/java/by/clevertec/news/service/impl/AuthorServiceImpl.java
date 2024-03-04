package by.clevertec.news.service.impl;

import by.clevertec.aspect.ServiceAspectLogger;
import by.clevertec.news.domain.Author;
import by.clevertec.news.repository.AuthorRepository;
import by.clevertec.news.service.AuthorService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    @Override
    @ServiceAspectLogger
    @Cacheable(value = "AuthorService::getByName", key = "#name")
    public Author getByName(String name) {
        Optional<Author> optionalAuthor = authorRepository.findByName(name);
        return optionalAuthor.orElseGet(() -> authorRepository.save(new Author(name)));
    }
}
