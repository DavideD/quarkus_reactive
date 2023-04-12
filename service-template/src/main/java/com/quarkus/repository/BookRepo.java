package com.quarkus.repository;

import java.time.Duration;
import javax.enterprise.context.ApplicationScoped;
import javax.persistence.PersistenceException;

import org.hibernate.reactive.mutiny.Mutiny;

import com.quarkus.entity.Book;
import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;

@ApplicationScoped
public class BookRepo implements PanacheRepository<Book> {

    // Got error java.util.concurrent.CompletionException:
    // org.hibernate.LazyInitializationException: HR000056:
    // Collection cannot be initialized: com.quarkus.entity.Author.books
    // - Fetch the collection using 'Mutiny.fetch', 'Stage.fetch', or 'fetch join' in HQL
    public Uni<Book> findAndFetch(Long id) {
        return findById(id)
                .call(book -> Mutiny.fetch(book.author))
                .call(book -> Mutiny.fetch(book.publishers));
    }

    public Uni<Book> save(Book book) {
        return Panache.withTransaction(book::persist)
                .replaceWith(book)
                .ifNoItem()
                .after(Duration.ofMillis(10000))
                .fail()
                .onFailure().transform(PersistenceException::new);
    }
}
