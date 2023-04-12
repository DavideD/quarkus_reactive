package com.quarkus.resource;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.quarkus.entity.Book;
import com.quarkus.repository.BookRepo;
import io.smallrye.mutiny.Uni;

@ApplicationScoped
public class BookResource {
    @Inject
    BookRepo bookRepo;

    public Uni<Book> find(Long id) {
        return bookRepo.findAndFetch(id);
    }

    public Uni<Book> findVO(Long id) {
        return bookRepo.findAndFetch(id);
    }

    public Uni<Book> findByName(String name) {
        return bookRepo.find("book_name", name).firstResult();
    }

    public Uni<Book> create(Book book) {
        return findByName(book.bookName)
                .invoke(result -> {
                    if (result != null) {
                        throw new RuntimeException("Duplicate"); //Duplicated
                    }
                })
                .replaceWith(book)
                .call(bookRepo::save);
    }
}
