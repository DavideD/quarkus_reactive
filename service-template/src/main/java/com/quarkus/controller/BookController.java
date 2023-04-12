package com.quarkus.controller;

import com.quarkus.entity.Book;
import com.quarkus.resource.BookResource;
import com.quarkus.vo.BookVO;
import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;
import org.jboss.resteasy.reactive.RestPath;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/book")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookController {

    @Inject
    BookResource bookResource;

    @GET
    @Path("{id}")
    public Uni<BookVO> get(@RestPath("id") Long id) {
        return bookResource.findVO(id)
                .onFailure().recoverWithItem(err -> {
                    Log.errorv("Get failed: {}", err);
                    return null;
                });

    }

    @POST
    public Uni<Book> create(Book author) {
        return bookResource.create(author)
                .onFailure().recoverWithItem(err -> {
                    Log.errorv("Create failed: {}", err);
                    return null;
                });
    }
}
