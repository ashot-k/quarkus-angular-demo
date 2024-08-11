package org.resources;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.entities.Book;
import org.jboss.resteasy.reactive.RestResponse;
import org.services.AuthorService;

import java.util.List;

@Path("/authors/{authorId}/books")
public class AuthorBooksResource {
    @Inject
    AuthorService authorService;

    @POST
    public RestResponse<Book> createAuthorBook(@PathParam("authorId") Long authorId, Book book) {
        Book savedBook = authorService.addBook(authorId, book);
        return RestResponse.status(RestResponse.Status.CREATED, savedBook);
    }
    @GET
    public RestResponse<List<Book>> getAuthorBooks(@PathParam("authorId") Long authorId) {
        return RestResponse.ok((authorService.getBooksByAuthor(authorId)));
    }
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public RestResponse<String> deleteAuthorBooks(@PathParam("authorId") Long authorId) {
        if(authorService.deleteBooks(authorId)) {
            return RestResponse.ok("Successfully deleted all books for author with id: " + authorId);
        }
        return RestResponse.status(Response.Status.INTERNAL_SERVER_ERROR, "Error when deleting books from author: " + authorId);
    }
}
