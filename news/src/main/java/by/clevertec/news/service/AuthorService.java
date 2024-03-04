package by.clevertec.news.service;

import by.clevertec.news.domain.Author;

public interface AuthorService {

    Author getByName(String name);
}
