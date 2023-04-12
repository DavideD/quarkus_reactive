package com.quarkus.controller;

import com.quarkus.entity.Author;
import com.quarkus.resource.AuthorResource;
import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.reactive.RestPath;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/author")
@Slf4j
public class AuthorController {

    @Inject
    AuthorResource authorResource;

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Author> get(@RestPath("id") Long id) {
        return authorResource.find(id)
                .onFailure().recoverWithItem(err -> {
                    Log.errorv("Get failed: {}", err);
                    return null;
                });

    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Author> create(Author author) {
        return authorResource.create(author)
                .onFailure().recoverWithItem(err -> {
                    Log.errorv("Create failed: {}", err);
                    return null;
                });
    }
}
