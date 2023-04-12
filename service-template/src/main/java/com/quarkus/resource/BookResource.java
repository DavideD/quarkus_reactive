package com.quarkus.resource;

import com.quarkus.entity.Book;
import com.quarkus.repository.BookRepo;
import com.quarkus.vo.BookVO;
import io.smallrye.mutiny.Uni;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class BookResource {
    @Inject
    BookRepo bookRepo;

    public Uni<Book> find(Long id) {
        return bookRepo.findAndFetch(id);
    }

    public Uni<BookVO> findVO(Long id) {
        return bookRepo.findFetchAndConvertVO(id);
    }

    public Uni<Book> findByName(String name) {
        return bookRepo.find("book_name", name).firstResult();
    }

    public Uni<Book> create(Book book) {
        return findByName(book.getBookName())
                .invoke(result -> {
                    if (result != null) {
                        throw new RuntimeException("Duplicate"); //Duplicated
                    }
                })
                .replaceWith(book)
                .call(bookRepo::save);
    }
}
