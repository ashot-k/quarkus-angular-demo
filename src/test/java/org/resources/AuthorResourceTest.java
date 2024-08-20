package org.resources;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.entities.Author;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.services.AuthorService;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@QuarkusTest
@ExtendWith(MockitoExtension.class)
public class AuthorResourceTest {

    @InjectMock
    AuthorService authorService;

    @Test
    void getAuthor_shouldReturnStatus400_whenNoIdSpecified() {
        given().pathParam("id", "ok").when().get("/authors/{id}").then().statusCode(400);
    }

    @Test
    void addAuthor_shouldReturnAuthorAndAllFields() {
        Author author = new Author("An author", (short) 25);
        Author authorWithId = new Author("An author", (short) 25);
        authorWithId.setId(1L);

        when(authorService.addAuthor(any(Author.class))).thenReturn(authorWithId);
        given()
                .body(author).contentType(ContentType.JSON)
                .when().post("/authors")
                .then()
                .body("id", equalTo(authorWithId.getId().intValue()))
                .body("name", equalTo(authorWithId.getName()))
                .body("age", equalTo((int) authorWithId.getAge()))
                .statusCode(201);
    }



}
