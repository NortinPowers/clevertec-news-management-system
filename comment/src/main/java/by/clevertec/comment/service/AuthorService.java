package by.clevertec.comment.service;


import by.clevertec.comment.domain.Author;

public interface AuthorService {

    Author getByName(String name);
}
