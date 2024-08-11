package org.services;

import org.entities.Book;
import org.request.BookRequestDTO;

import java.util.List;

public interface BookService {
    List<Book> getAllBooks();
    Book getBookById(Long id);
    Book updateBook(Long id, BookRequestDTO book);
    void deleteBook(Long id);
}
