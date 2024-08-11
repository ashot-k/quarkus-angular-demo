package org.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.entities.Author;
import org.entities.Book;
import org.exceptions.EntityNotFoundMessage;
import org.repositories.AuthorRepository;
import org.repositories.BookRepository;
import org.request.BookRequestDTO;

import java.util.List;

@ApplicationScoped
public class BookServiceImpl implements BookService {

    @Inject
    BookRepository bookRepo;
    @Inject
    AuthorRepository authorRepo;
    @Inject
    EntityManager entityManager;

    @Override
    public List<Book> getAllBooks() {
        return bookRepo.listAll();
    }

    @Override
    public Book getBookById(Long id) {
        return bookRepo.findById(id);
    }

    @Override
    @Transactional
    public Book updateBook(Long bookId, BookRequestDTO bookRequestDTO) {
        Book oldBook = bookRepo.findByIdOptional(bookId).orElseThrow(()-> new EntityNotFoundException(EntityNotFoundMessage.createMessage(bookId, Book.class)));

        List<Author> newAuthorsOfBook = authorRepo.findAuthorsByIds(bookRequestDTO.authorIds());
        if(newAuthorsOfBook.isEmpty()){
            throw new EntityNotFoundException("No authors found with requested Ids");
        }
        else if (newAuthorsOfBook.size() != bookRequestDTO.authorIds().size()){
            throw new EntityNotFoundException("Some specified authors could not be found");
        }
        String removeOldAssocQuery = "DELETE FROM author_book ab WHERE ab.book_id = :bookId";
        var removeOldAssoc = entityManager.createNativeQuery(removeOldAssocQuery);
        removeOldAssoc.setParameter("bookId", bookId);
        removeOldAssoc.executeUpdate();
        oldBook.setTitle(bookRequestDTO.title());
        oldBook.setImage(bookRequestDTO.image());
        oldBook.setAuthors(newAuthorsOfBook);
        for (Author author : newAuthorsOfBook) {
            if(!author.getBooks().contains(oldBook)) {
                author.getBooks().add(oldBook);
            }
            else {
                author.getBooks().set(author.getBooks().indexOf(oldBook), oldBook);
            }
        }
        bookRepo.persist(oldBook);
        return oldBook;
    }
    /*@Override
    @Transactional
    public Book updateBook(Long id, BookRequestDTO bookRequestDTO) {
        Book oldBook = bookRepo.findByIdOptional(id).orElseThrow(EntityNotFoundException::new);

        List<Author> newAuthorsOfBook = authorRepo.findAuthorsByIds(bookRequestDTO.authorIds());
        if(newAuthorsOfBook.isEmpty()){
            throw new EntityNotFoundException("No authors found with requested Ids");
        }

        String removeOldAssocsQuery = "DELETE FROM author_book ab WHERE ab.book_id = :bookId";
        var removeOldAssocs = entityManager.createNativeQuery(removeOldAssocsQuery);
        removeOldAssocs.setParameter("bookId", id);
        removeOldAssocs.executeUpdate();
        oldBook.setTitle(bookRequestDTO.title());
        oldBook.setImage(bookRequestDTO.image());

        for (Author author : newAuthorsOfBook) {
            if(!author.getBooks().contains(oldBook)) {
                author.getBooks().add(oldBook);
            }
            else {
                author.getBooks().set(author.getBooks().indexOf(oldBook), oldBook);
            }
        }
        bookRepo.persist(oldBook);
        return oldBook;
    }*/

    @Override
    @Transactional
    public void deleteBook(Long id) {
        Book book = bookRepo.findById(id);
        List<Author> authors = book.getAuthors();
        for (Author author : authors) {
            author.getBooks().remove(book);  // Remove the book from the author's book list
        }
        bookRepo.delete(book);
    }
}
