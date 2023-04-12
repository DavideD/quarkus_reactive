package com.quarkus.resource;

import com.quarkus.entity.Author;
import com.quarkus.repository.AuthorRepo;
import io.smallrye.mutiny.Uni;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class AuthorResource {

    @Inject
    AuthorRepo authorRepo;

    public Uni<Author> find(Long id) {
        return authorRepo.findAndFetch(id);
    }

    public Uni<Author> findByName(String name) {
        return authorRepo.find("author_name", name).firstResult();
    }

    public Uni<Author> create(Author author) {
        return findByName(author.authorName)
                .invoke(result -> {
                    if (result != null) {
                        throw new RuntimeException("Duplicate"); //Duplicated
                    }
                })
                .replaceWith(author)
                .call(authorRepo::save);
    }
}
