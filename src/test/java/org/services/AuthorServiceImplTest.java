package org.services;

import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException;
import org.entities.Author;
import org.entities.Book;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.repositories.BookRepository;

import java.util.List;
import java.util.logging.Logger;

@QuarkusTest
public class AuthorServiceImplTest {
    private static final Logger LOGGER = Logger.getLogger(AuthorServiceImplTest.class.getName());

    @Inject
    AuthorService authorService;
    @Inject
    BookRepository bookRepo;

    @BeforeAll
    public static void setup() {
        LOGGER.info("Starting AuthorServiceImplTests");
    }

    @Test
    @TestTransaction
    public void addAuthor_ShouldCreateAuthorAndIncludedBooks() {
        LOGGER.info("Test case: addAuthor_ShouldCreateAuthorAndIncludedBooks");
        int expected = 2;

        Book book1 = new Book("book1", "path/to/image");
        Book book2 = new Book("book2", "");
        Author author = new Author("John Doe", (short) 24);
        author.setBooks(List.of(book1, book2));

        authorService.addAuthor(author);
        List<Author> list = authorService.getAuthors();
        Assertions.assertTrue(list.contains(author));
        Author savedAuthor = list.getFirst();
        Assertions.assertEquals(expected, savedAuthor.getBooks().size());
    }

    @Test
    @TestTransaction
    public void update_shouldUpdatePersistedAge_whenChangingAuthorAge() {
        LOGGER.info("Test case: update_shouldUpdatePersistedAge_whenChangingAuthorAge");
        short age = 24;
        short agePostUpdate = 30;

        Author author = new Author("John Doe", age);
        Book book1 = new Book("book1", "path/to/image");
        author.setBooks(List.of(book1));
        authorService.addAuthor(author);

        Author savedAuthor = Author.findById(author.getId());
        savedAuthor.setAge(agePostUpdate);
        authorService.updateAuthor(savedAuthor, savedAuthor.getId());

        Author updatedAuthor = Author.findById(author.getId());
        Assertions.assertEquals(agePostUpdate, updatedAuthor.getAge());
    }

    @Test
    @TestTransaction
    public void updateAuthor_shouldUpdatePersistedBooks_whenChangingAuthorsBooks() {
        LOGGER.info("Test case: updateAuthor_shouldUpdatePersistedBooks_whenChangingAuthorsBooks");
        Author author = new Author("John Doe", (short) 25);
        Book book1 = new Book("book1", "path/to/image1");

        author.setBooks(List.of(book1));

        authorService.addAuthor(author);
        Book book2 = new Book("book2", "path/to/image2");
        Book book3 = new Book("book3", "path/to/image3");

        author.setBooks(List.of(book1, book2, book3));
        authorService.updateAuthor(author, author.getId());
        List<Author> list = Author.findAll().list();
        Author updatedAuthor = list.getFirst();
        Assertions.assertEquals(3, updatedAuthor.getBooks().size());
    }
    @Test
    @TestTransaction
    public void deleteAuthor_ShouldNotDeleteBooks(){
        LOGGER.info("Test case: deleteAuthor_ShouldNotDeleteBooks");

        Author author = new Author("John Doe", (short) 25);
        Book book1 = new Book("book1", "path/to/image1");
        author.setBooks(List.of(book1));
        authorService.addAuthor(author);

        long initBookCount = bookRepo.count();
        Assertions.assertTrue(authorService.deleteAuthor(author.getId()));
        Assertions.assertEquals(initBookCount, bookRepo.count());
    }
    @Test
    @TestTransaction
    public void getBooksByAuthor_shouldReturnOnlySpecifiedAuthorsBooks(){
        LOGGER.info("Test case: getBooksByAuthor_shouldReturnOnlySpecifiedAuthorsBooks");
        Author author = new Author("John Doe", (short) 25);
        Book book1 = new Book("book1", "path/to/image1");
        author.setBooks(List.of(book1));
        authorService.addAuthor(author);

        Author author2 = new Author("John Doe 2", (short) 22);
        Book book2 = new Book("book2", "path/to/image2");
        Book book3 = new Book("book3", "path/to/image3");
        author2.setBooks(List.of(book2, book3));
        authorService.addAuthor(author2);

        Assertions.assertEquals(2, authorService.getBooksByAuthor(author2.getId()).size());
    }

    @Test
    @TestTransaction
    public void addBook_shouldThrowEntityNotFoundException_whenAuthorDoesNotExist(){
        LOGGER.info("Test case: addBook_shouldThrowEntityNotFoundException_whenAuthorDoesNotExist");
        Book book1 = new Book("book1", "path/to/image1");
        Assertions.assertThrows(EntityNotFoundException.class, ()-> authorService.addBook(1L, book1));
    }

    @Test
    @TestTransaction
    public void addBook_shouldAddBookToAuthorsBooks(){
        LOGGER.info("Test case: addBook_shouldAddBookToAuthorsBooks");

        Author author = new Author("John Doe", (short) 25);
        Book book1 = new Book("book1", "path/to/image1");
        author.setBooks(List.of(book1));
        authorService.addAuthor(author);

        Book book2 = new Book("book2", "path/to/image2");
        Book book3 = new Book("book3", "path/to/image3");
        authorService.addBook(author.getId(), book2);
        authorService.addBook(author.getId(), book3);

        Assertions.assertEquals(3, author.getBooks().size());
    }


    @Test
    @TestTransaction
    public void deleteBooks_shouldRemoveAllBooks(){
        LOGGER.info("Test case: deleteBooks_shouldRemoveAllBooks");

        Author author = new Author("John Doe", (short) 25);
        Book book1 = new Book("book1", "path/to/image1");
        author.setBooks(List.of(book1));
        authorService.addAuthor(author);

        Assertions.assertTrue(authorService.deleteBooks(author.getId()));
        Assertions.assertTrue(bookRepo.findAll().list().isEmpty());
    }




}
