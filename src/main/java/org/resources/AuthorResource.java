package org.resources;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.entities.Author;
import org.jboss.resteasy.reactive.RestResponse;
import org.services.AuthorService;

import java.util.List;

@Path("/authors")
public class AuthorResource {

    @Inject
    AuthorService authorService;

    @POST
    public RestResponse<Author> addAuthor(@Valid Author author) {
        return RestResponse.status(RestResponse.Status.CREATED, authorService.addAuthor(author));
    }

    @GET
    public RestResponse<List<Author>> getAuthorsPaged(@DefaultValue("0") @QueryParam("pageNo") int pageNo,
                                                      @DefaultValue("25") @QueryParam("pageSize") int pageSize) {
        return RestResponse.ok(authorService.getAuthorsPaged(pageNo, pageSize));
    }

    @GET
    @Path("/{id}")
    public RestResponse<Author> getAuthor(@PathParam("id") String id) {
        return RestResponse.ok(authorService.getAuthor(Long.parseLong(id)));
    }

    @PUT
    @Path("/{id}")
    public RestResponse<Author> updateAuthor(@PathParam("id") Long id, Author author) {
        return RestResponse.ok(authorService.updateAuthor(author, id));
    }

    @DELETE
    @Path("/{id}")
    public RestResponse<String> deleteAuthor(@PathParam("id") Long id) {
        if (authorService.deleteAuthor(id)) {
            return RestResponse.ok("Author: " + id + " deleted");
        }
        return RestResponse.status(Response.Status.INTERNAL_SERVER_ERROR, "Error when deleting author: " + id);
    }

}
