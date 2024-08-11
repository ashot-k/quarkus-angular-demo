package org.services;

import org.entities.Author;
import org.entities.Book;

import java.util.List;

public interface AuthorService {
    Author addAuthor(Author author);
    List<Author> getAuthors();
    List<Author> getAuthorsPaged(int pageNo, int pageSize);
    Author getAuthor(Long id);
    Author updateAuthor(Author author, Long id);
    boolean deleteAuthor(Long id);

    Book addBook(Long authorId, Book book);
    List<Book> getBooksByAuthor(Long authorId);
    boolean deleteBooks(Long authorId);
}
