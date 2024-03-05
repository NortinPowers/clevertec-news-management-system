package by.clevertec.comment.service;

import by.clevertec.comment.domain.Author;

public interface AuthorService {

    /**
     * Получает объект {@link Author} по уникальному имени (String).
     * Реализация должна обеспечить создание нового автора с указанным именем, если автор не найден.
     *
     * @param name Уникальное имя (String) объекта {@link Author}.
     * @return Объект {@link Author}, найденный по указанному имени.
     */
    Author getByName(String name);
}
