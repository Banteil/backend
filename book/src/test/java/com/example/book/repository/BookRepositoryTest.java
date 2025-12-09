package com.example.book.repository;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.book.entity.Book;

import jakarta.persistence.EntityNotFoundException;

@SpringBootTest
class BookRepositoryTest {
	@Autowired
	private BookRepository bookRepository;

	@Test
	public void readTest() {
		Book result = bookRepository.findById(1L).get();
		System.out.println(result.toString());
	}

	@Test
	public void readTest2() {
		Book result = bookRepository.findFirstByIsbn("B00001").orElseThrow(EntityNotFoundException::new);
		System.out.println(result);
	}

	@Test
	public void readAllTest() {
		List<Book> results = bookRepository.findAll();
		results.forEach((e) -> {
			System.out.println(e.toString());
		});
	}

	@Test
	public void deleteTest() {
		bookRepository.deleteById(2L);
	}

	@Test
	public void updateTest() {
		Book book = bookRepository.findFirstByIsbn("B99999").orElseThrow();
		book.setPrice(20000);
		bookRepository.save(book);
	}

	@Test
	public void insertTest() {
		Book student = Book.builder()
				.isbn("B99999")
				.title("네크로노미콘")
				.price(10000)
				.author("알 아지프")
				.build();

		// insert(c), update(u) 작업 시 호출
		bookRepository.save(student);
	}

	@Test
	public void insertArrayTest() {
		List<Book> bookList = new ArrayList<>();
		for (int i = 1; i <= 100; i++) {
			Book book = Book.builder()
					.isbn("B" + String.format("%05d", i))
					.title("책 " + i)
					.price(10000)
					.author("성춘향")
					.build();
			bookList.add(book);
		}

		bookRepository.saveAll(bookList);
	}

	@Test
	public void testFindBy() {
		List<Book> list = bookRepository.findByAuthor("성춘");
		System.out.println(list);

		List<Book> list2 = bookRepository.findByAuthorEndingWith("향");
		System.out.println(list2);

		List<Book> list3 = bookRepository.findByAuthorStartingWith("알");
		System.out.println(list3);
	}
}
