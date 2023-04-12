package com.quarkus.repository;

import java.time.Duration;
import javax.enterprise.context.ApplicationScoped;
import javax.persistence.PersistenceException;

import org.hibernate.reactive.mutiny.Mutiny;

import com.quarkus.entity.Author;
import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;

@ApplicationScoped
public class AuthorRepo implements PanacheRepository<Author> {

    //If an author has >= 1 book, got error:
    //HR000057: java.util.concurrent.CompletionException: org.hibernate.LazyInitializationException:
    //HR000056: Collection cannot be initialized: com.quarkus.entity.Author.books
    // - Fetch the collection using 'Mutiny.fetch', 'Stage.fetch', or 'fetch join' in HQL
    public Uni<Author> findAndFetch(Long id) {
        return find( "select a from Author a left join fetch a.books b left join fetch b.publishers where a.id = ?1", id )
                .firstResult();
    }

    public Uni<Author> save(Author author) {
        return Panache.withTransaction(author::persist)
                .replaceWith(author)
                .ifNoItem()
                .after(Duration.ofMillis(10000))
                .fail()
                .onFailure().transform(PersistenceException::new);
    }
}
