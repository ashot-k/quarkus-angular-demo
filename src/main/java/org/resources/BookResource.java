package org.resources;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.entities.Book;
import org.jboss.resteasy.reactive.RestResponse;
import org.request.BookRequestDTO;
import org.services.BookService;

import java.util.List;

@Path("/books")
public class BookResource {

    @Inject
    BookService bookService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Book> getBooks() {
        return bookService.getAllBooks();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public RestResponse<Book> getBook(@PathParam("id") Long id) {
        return RestResponse.ok(bookService.getBookById(id));
    }

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public RestResponse<Book> updateBook(@PathParam("id") Long id, BookRequestDTO bookDTO) {
        return RestResponse.ok(bookService.updateBook(id, bookDTO));
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteBook(@PathParam("id") Long id) {
        bookService.deleteBook(id);
        return Response.ok("Removed book with id: " + id).build();
    }
}
