package com.quarkus.controller;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.reactive.RestPath;

import com.quarkus.entity.Book;
import com.quarkus.resource.BookResource;
import io.smallrye.mutiny.Uni;

@Path("/book")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookController {

    @Inject
    BookResource bookResource;

    @GET
    @Path("{id}")
    public Uni<Book> get(@RestPath("id") Long id) {
        return bookResource.find(id);

    }

    @POST
    public Uni<Book> create(Book author) {
        return bookResource.create(author);
    }
}
