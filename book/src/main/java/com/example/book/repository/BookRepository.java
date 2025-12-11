package com.example.book.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.example.book.entity.Book;
import com.example.book.entity.QBook;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

//DAO 역할
// 기본적인 CRUD 메소드는 이미 정의되어 있음
//<클래스, id type>
public interface BookRepository extends JpaRepository<Book, Long>, QuerydslPredicateExecutor<Book> {
    Optional<Book> findFirstByIsbn(String isbn);

    Optional<Book> findFirstByTitle(String title);

    List<Book> findByTitleContaining(String title);

    List<Book> findByAuthor(String author);

    // where author like '%영'
    List<Book> findByAuthorStartingWith(String author);

    // where author like '박%'
    List<Book> findByAuthorEndingWith(String author);

    // where author like '%진수%'
    List<Book> findByAuthorContaining(String author);

    // ~이상 ~ 이하
    List<Book> findByPriceBetween(int startPrice, int endPrice);

    public default Predicate makePredicate(String type, String keyword) {
        BooleanBuilder builder = new BooleanBuilder();
        QBook book = QBook.book;
        builder.and(book.id.gt(0));
        if (type == null || type.isEmpty() || keyword == null || keyword.isEmpty()) {
            return builder;
        }
        switch (type) {
            case "t":
                builder.and(book.title.contains(keyword));
                break;
            case "a":
                builder.and(book.author.contains(keyword));
                break;
        }
        return builder;
    }
}
