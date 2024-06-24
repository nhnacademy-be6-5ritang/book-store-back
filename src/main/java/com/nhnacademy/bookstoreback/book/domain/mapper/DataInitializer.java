package com.nhnacademy.bookstoreback.book.domain.mapper;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.nhnacademy.bookstoreback.book.domain.entity.Author;
import com.nhnacademy.bookstoreback.book.domain.entity.Book;
import com.nhnacademy.bookstoreback.book.domain.entity.BookStatus;
import com.nhnacademy.bookstoreback.book.domain.entity.Publisher;
import com.nhnacademy.bookstoreback.book.repository.AuthorRepository;
import com.nhnacademy.bookstoreback.book.repository.BookRepository;
import com.nhnacademy.bookstoreback.book.repository.BookStatusRepository;
import com.nhnacademy.bookstoreback.book.repository.PublisherRepository;

@Component
public class DataInitializer implements CommandLineRunner {

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private AuthorRepository authorRepository;

	@Autowired
	private PublisherRepository publisherRepository;

	@Autowired
	private BookStatusRepository bookStatusRepository;

	@Override
	public void run(String... args) throws Exception {
		Author author = new Author();
		author.setAuthorName("John Doe");

		authorRepository.save(author);

		Publisher publisher = new Publisher();
		publisher.setPublisherName("엄준식 파인더 출판사");

		publisherRepository.save(publisher);

		BookStatus bookStatus = new BookStatus();
		bookStatus.setBookStatusName("ON_SALE");

		bookStatusRepository.save(bookStatus);

		Book book = new Book();
		book.setBookTitle("Sample Book Title");
		book.setBookIndex("1");
		book.setBookDescription("Sample description");
		book.setBookPackaging(false);
		book.setBookPublishDate(new Date());
		book.setBookIsbn("978-3-16-148410-0");
		book.setBookPrice(BigDecimal.valueOf(29.99));
		book.setBookSalePercent(BigDecimal.valueOf(10));
		book.setBookSalePrice(BigDecimal.valueOf(26.99));
		book.setAuthor(author);
		book.setPublisher(publisher);
		book.setBookStatus(bookStatus);

		bookRepository.save(book);
	}
}
