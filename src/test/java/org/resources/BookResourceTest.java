package org.resources;


import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import org.entities.Book;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.services.BookService;

import java.util.List;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class BookResourceTest{

    @InjectMock
    BookService bookService;

    @BeforeAll
    public static void setup() {

    }
    @Test
    public void testGetBooks() {

        Mockito.when(this.bookService.getAllBooks()).thenReturn(List.of(new Book("a new book", "path/to/image")));
        given().when().get("/books").then().statusCode(200);
    }


}
