package com.quarkus.repository;

import com.quarkus.entity.Author;
import com.quarkus.vo.AuthorVO;
import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;
import org.hibernate.reactive.mutiny.Mutiny;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.PersistenceException;
import java.time.Duration;

@ApplicationScoped
public class AuthorRepo implements PanacheRepository<Author> {

    //If an author has >= 1 book, got error:
    //HR000057: java.util.concurrent.CompletionException: org.hibernate.LazyInitializationException:
    //HR000056: Collection cannot be initialized: com.quarkus.entity.Author.books
    // - Fetch the collection using 'Mutiny.fetch', 'Stage.fetch', or 'fetch join' in HQL
    public Uni<Author> findAndFetch(Long id) {
        return findById(id)
                .call(author -> Mutiny.fetch(author.getBooks()));
    }

    // I solved the issue with this function, but I want to reuse Author - for some reasons
    public Uni<AuthorVO> findFetchAndConvertVO(Long id) {
        return findById(id)
                .call(author -> Mutiny.fetch(author.getBooks()))
                .map(author -> AuthorVO.builder()
                        .id(author.getId())
                        .authorName(author.getAuthorName())
                        .build()
                );
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
