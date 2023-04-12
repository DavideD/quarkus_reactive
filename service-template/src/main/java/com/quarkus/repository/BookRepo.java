package com.quarkus.repository;

import com.quarkus.entity.Book;
import com.quarkus.vo.AuthorVO;
import com.quarkus.vo.BookVO;
import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;
import org.hibernate.reactive.mutiny.Mutiny;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.PersistenceException;
import java.time.Duration;

@ApplicationScoped
public class BookRepo implements PanacheRepository<Book> {

    // Got error java.util.concurrent.CompletionException:
    // org.hibernate.LazyInitializationException: HR000056:
    // Collection cannot be initialized: com.quarkus.entity.Author.books
    // - Fetch the collection using 'Mutiny.fetch', 'Stage.fetch', or 'fetch join' in HQL
    public Uni<Book> findAndFetch(Long id) {
        return findById(id)
                .call(book -> Mutiny.fetch(book.getAuthor()))
                .call(book -> Mutiny.fetch(book.getPublishers()));
    }

    // I solved the issue with this function, but I want to reuse Book - for some reasons
    public Uni<BookVO> findFetchAndConvertVO(Long id) {
        return findById(id)
                .call(book -> Mutiny.fetch(book.getAuthor()))
                .call(book -> Mutiny.fetch(book.getPublishers()))
                .map(book -> BookVO.builder()
                        .id(book.getId())
                        .bookName(book.getBookName())
                        .author(new AuthorVO(book.getAuthor().getId(), book.getAuthor().getAuthorName()))
                        .build()
                );
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
