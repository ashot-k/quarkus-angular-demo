package org.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.entities.Author;
import org.entities.Book;
import org.exceptions.EntityNotFoundMessage;
import org.repositories.AuthorRepository;
import org.repositories.BookRepository;

import java.util.ArrayList;
import java.util.List;

import static org.exceptions.EntityNotFoundMessage.createMessage;

@ApplicationScoped
public class AuthorServiceImpl implements AuthorService {

    @Inject
    AuthorRepository authorRepo;
    @Inject
    BookRepository bookRepo;

    @Override
    public List<Author> getAuthors() {
        return authorRepo.listAll();
    }

    @Override
    public List<Author> getAuthorsPaged(int pageNo, int pageSize) {
        return authorRepo.findAuthorsPaged(pageNo, pageSize);
    }

    @Override
    public Author getAuthor(Long id) {
        return authorRepo.findByIdOptional(id).orElseThrow(()->new EntityNotFoundException(EntityNotFoundMessage.createMessage(id, Author.class)));
    }

    @Override
    @Transactional
    public Author addAuthor(Author author) {
        authorRepo.persist(author);
        return author;
    }

    @Override
    @Transactional
    public Author updateAuthor(Author author, Long id) {
        Author oldAuthor = authorRepo.findByIdOptional(id).orElseThrow(()->new EntityNotFoundException(EntityNotFoundMessage.createMessage(id, Author.class)));
        oldAuthor.setAge(author.getAge());
        oldAuthor.setName(author.getName());
        oldAuthor.setBooks(author.getBooks());
        authorRepo.persist(oldAuthor);
        return oldAuthor;
    }

    @Override
    @Transactional
    public boolean deleteAuthor(Long id) {
        return authorRepo.deleteById(id);
    }

    @Override
    public List<Book> getBooksByAuthor(Long authorId) {
        return authorRepo.findBooksByAuthorId(authorId);
    }

    @Override
    @Transactional
    public Book addBook(Long authorId, Book book) {
        Author author = authorRepo.findByIdOptional(authorId).orElseThrow(() -> new EntityNotFoundException(createMessage(authorId, Author.class)));
        List<Book> authorBooks = new ArrayList<>(author.getBooks());
        authorBooks.add(book);
        author.setBooks(authorBooks);
        authorRepo.persist(author);
        return book;
    }

    @Override
    @Transactional
    public boolean deleteBooks(Long authorId) {
        Author author = authorRepo.findById(authorId);
        if (author == null) {
            throw new EntityNotFoundException(createMessage(authorId, Author.class));
        }
        if (author.getBooks() != null && !author.getBooks().isEmpty()) {
            for (Book book : author.getBooks()) {
                bookRepo.delete(book);
            }
            List<Book> books = new ArrayList<>(author.getBooks());
            books.clear();
            author.setBooks(books);
        } else {
            throw new EntityNotFoundException("Specified Author does not have any books");
        }
        authorRepo.persist(author);
        return author.getBooks().isEmpty();
    }

}
