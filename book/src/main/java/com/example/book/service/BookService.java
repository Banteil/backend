package com.example.book.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.book.dto.BookDTO;
import com.example.book.entity.Book;
import com.example.book.repository.BookRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RequiredArgsConstructor
@Log4j2
@Service
public class BookService {
    private final BookRepository bookRepository;
    private final ModelMapper modelMapper;

    public Long insert(BookDTO dto) {
        // dto => entity
        Book book = modelMapper.map(dto, Book.class);
        return bookRepository.save(book).getId();
    }

    public BookDTO read(Long id) {
        // NoSuchElementException
        Book book = bookRepository.findById(id).orElseThrow();
        // entity => dto 변환 후 리턴
        return modelMapper.map(book, BookDTO.class);
    }

    public List<BookDTO> readTitle(String title) {
        List<Book> result = bookRepository.findByTitleContaining(title);

        // 이제 .map(book -> ...) 에서 book이 Book 타입임을 명확히 알 수 있습니다.
        List<BookDTO> list = result.stream()
                .map(book -> modelMapper.map(book, BookDTO.class))
                .collect(Collectors.toList());
        return list;
    }

    public BookDTO readIsbn(String isbn) {
        Book result = bookRepository.findFirstByIsbn(isbn).orElseThrow();
        return modelMapper.map(result, BookDTO.class);
    }

    public List<BookDTO> readAll() {
        List<Book> books = bookRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));

        List<BookDTO> list = books.stream()
                .map(Book::toDTO)
                .collect(Collectors.toList());

        return list;
    }

    public Page<BookDTO> readAll(Pageable pageable) {
        Page<Book> bookPage = bookRepository.findAll(pageable);
        Page<BookDTO> dtoPage = bookPage.map(Book::toDTO);
        return dtoPage;
    }

    public Long update(BookDTO dto) {
        Book book = bookRepository.findById(dto.getId())
                .orElseThrow(() -> new IllegalArgumentException("책을 찾을 수 없습니다."));
        modelMapper.map(dto, book);
        return bookRepository.save(book).getId();
    }

    public void delete(Long id) {
        bookRepository.deleteById(id);
    }

}
