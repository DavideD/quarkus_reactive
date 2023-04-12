package com.quarkus.controller;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.reactive.RestPath;

import com.quarkus.entity.Author;
import com.quarkus.resource.AuthorResource;
import io.smallrye.mutiny.Uni;

@Path("/author")
public class AuthorController {

    @Inject
    AuthorResource authorResource;

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Author> get(@RestPath("id") Long id) {
        return authorResource
                .find(id)
                .invoke( author -> System.out.println( author ) );

    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Author> create(Author author) {
        return authorResource.create(author);
    }
}
