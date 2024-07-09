package com.nhnacademy.bookstoreback.image.service;

import com.nhnacademy.bookstoreback.book.domain.entity.Book;
import com.nhnacademy.bookstoreback.book.repository.BookRepository;
import com.nhnacademy.bookstoreback.image.domain.entity.Image;
import com.nhnacademy.bookstoreback.book.domain.entity.BookImage;
import com.nhnacademy.bookstoreback.image.repository.ImageRepository;
import com.nhnacademy.bookstoreback.image.repository.BookImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class BookImageService {

	private final BookRepository bookRepository;
	private final ImageRepository imageRepository;
	private final BookImageRepository bookImageRepository;

	@Autowired
	public BookImageService(BookRepository bookRepository, ImageRepository imageRepository, BookImageRepository bookImageRepository) {
		this.bookRepository = bookRepository;
		this.imageRepository = imageRepository;
		this.bookImageRepository = bookImageRepository;
	}

	@Transactional
	public void mapImageToBook(Book book) {
		Optional<Image> existingImageOptional = imageRepository.findByImageName(book.getBookTitle());

		Image image;
		if (existingImageOptional.isPresent()) {
			image = existingImageOptional.get();
		} else {
			image = new Image(book.getBookTitle(), "http://image.toast.com/aaaacuf/5ritang/books/null.jpg"); // 기본 URL을 사용
			imageRepository.save(image);
		}

		if (!bookImageRepository.existsByBookAndImage(book, image)) {
			BookImage bookImage = new BookImage();
			bookImage.setBook(book);
			bookImage.setImage(image);
			bookImageRepository.save(bookImage);
		}
	}

	@Transactional
	public void mapAllBooksToImages() {
		List<Book> books = bookRepository.findAll();
		for (Book book : books) {
			mapImageToBook(book);
		}
	}

	@Transactional
	public void mapImageForSingleBook(Book book) {
		mapImageToBook(book);
	}
}
