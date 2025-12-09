package com.example.book.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.book.entity.Book;

//DAO 역할
// 기본적인 CRUD 메소드는 이미 정의되어 있음
//<클래스, id type>
public interface BookRepository extends JpaRepository<Book, Long> {
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
}
