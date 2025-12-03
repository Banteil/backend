package com.example.book.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.book.dto.BookDTO;
import com.example.book.entity.Book;
import com.example.book.repository.BookRepository;

@SpringBootTest
class BookServiceTest {
	@Autowired
	private BookService bookService;
	@Autowired
	private BookRepository bookRepository;
	@Autowired
	private ModelMapper mapper;

	@Test
	public void testInsert() {
		BookDTO dto = BookDTO.builder()
				.isbn("B00001")
				.title("책")
				.build();

		System.out.println(bookService.insert(dto));
	}

	@Test
	public void testRead() {
		System.out.println(bookService.read(1L));
	}

	@Test
	public void testReadAll() {
		List<BookDTO> students = bookService.readAll();
		String formattedOutput = String.join("\n", students.stream()
				.map(Object::toString)
				.collect(Collectors.toList()));
		System.out.println(formattedOutput);
	}

	@Test
	public void testUpdate() {
		BookDTO dto = BookDTO.builder()
				.isbn("B00001")
				.title("책")
				.build();
		Long newId = bookService.insert(dto);
		dto.setId(newId);

		Long id = bookService.update(dto);
		System.out.println(bookService.read(id));
	}

	@Test
	public void testDelete() {
		List<BookDTO> students = bookService.readAll();
		Long id = students.get(students.size() - 1).getId();
		bookService.delete(id);
	}

	@Test
	public void readTitle() {
		List<Book> result = bookRepository.findByTitleContaining("책");
		List<BookDTO> list = result.stream()
				.map(book -> mapper.map(book, BookDTO.class))
				.collect(Collectors.toList());
		System.out.println(list);
	}
}
