package org.services;

import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.entities.Author;
import org.entities.Book;
import org.junit.jupiter.api.*;
import org.repositories.AuthorRepository;
import org.repositories.BookRepository;
import org.request.BookRequestDTO;

import java.util.List;
import java.util.logging.Logger;

@QuarkusTest
public class BookServiceImplTest {

    private static final Logger LOGGER = Logger.getLogger(BookServiceImplTest.class.getName());

    @Inject
    BookServiceImpl bookServiceImpl;
    @Inject
    BookRepository bookRepo;
    @Inject
    AuthorRepository authorRepo;

    Long testBookId;
    Long testAuthorId;
    Long testAuthorId2;
    @BeforeAll
    public static void setup() {
        LOGGER.info("Starting BookServiceImplTests");
    }
    @BeforeEach
    @Transactional
    public void addAuthorAndBookBeforeTests(){
        Author author = new Author("James", (short) 25);
        Author author2 = new Author("James2", (short) 25);
        Book book = new Book("A new Book", "path/to/image");
        author.setBooks(List.of(book));
        authorRepo.persist(author);
        authorRepo.persist(author2);
        this.testBookId = author.getBooks().getFirst().getId();
        this.testAuthorId = author.getId();
        this.testAuthorId2 = author2.getId();
    }

    @AfterEach
    @Transactional
    public void reset(){
        authorRepo.deleteAll();
        bookRepo.deleteAll();
    }

    @Test
    @TestTransaction
    public void deleteBook_shouldDeleteBookFromAuthorToo(){
        LOGGER.info("Test case: deleteBook_shouldDeleteBookFromAuthorToo");
        Author author = authorRepo.findById(testAuthorId);
        long initAuthorBookCount = author.getBooks().size();
        long initCount = bookRepo.count();
        bookServiceImpl.deleteBook(this.testBookId);
        Assertions.assertEquals(initCount - 1, bookRepo.count());
        Assertions.assertEquals(initAuthorBookCount - 1, author.getBooks().size());
    }


    @Test
    @TestTransaction
    public void updateBook_shouldChangeAuthorAndDetails_whenAuthorIsDifferent(){
        LOGGER.info("Test case: updateBook_shouldChangeAuthorAndDetails_whenAuthorIsDifferent");
        Long changedAuthorId = testAuthorId2;
        Book newBookDetails = new Book("An updated Book", "editedPath/to/image");
        Book updatedBook = bookServiceImpl.updateBook(this.testBookId, new BookRequestDTO(newBookDetails.getTitle(), List.of(this.testAuthorId2), newBookDetails.getImage()));
        Assertions.assertEquals(newBookDetails.getTitle(), updatedBook.getTitle());
        Assertions.assertEquals(newBookDetails.getImage(), updatedBook.getImage());
        Assertions.assertEquals(changedAuthorId, updatedBook.getAuthors().getFirst().getId());
        Assertions.assertEquals(changedAuthorId, authorRepo.findById(updatedBook.getAuthors().getFirst().getId()).getId());
    }

}
