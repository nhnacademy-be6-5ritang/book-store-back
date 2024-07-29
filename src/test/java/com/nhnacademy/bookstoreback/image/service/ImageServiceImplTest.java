package com.nhnacademy.bookstoreback.image.service;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.nhnacademy.bookstoreback.book.domain.entity.Book;
import com.nhnacademy.bookstoreback.image.repository.BookImageRepository;
import com.nhnacademy.bookstoreback.image.repository.ImageRepository;

class BookImageServiceTest {

	@InjectMocks
	private BookImageService bookImageService;

	@Mock
	private ImageRepository imageRepository;

	@Mock
	private BookImageRepository bookImageRepository;

	private Book book;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);

		book = Book.builder().bookTitle("Test Book").build();
	}

	@Test
	void testMapImageToBook_WithValidUrl() {
		String imageUrl = "http://example.com/test-image.jpg";

		bookImageService.mapImageToBook(book, imageUrl);

		verify(imageRepository).save(argThat(image ->
			"Test Book.jpg".equals(image.getImageName()) &&
				imageUrl.equals(image.getImageUrl())
		));
		verify(bookImageRepository).save(argThat(bookImage ->
			book.equals(bookImage.getBook()) &&
				"Test Book.jpg".equals(bookImage.getImage().getImageName()) &&
				imageUrl.equals(bookImage.getImage().getImageUrl())
		));
	}

	@Test
	void testMapImageToBook_WithNullUrl() {
		String defaultImageUrl = "http://image.toast.com/aaaacuf/5ritang/books/null.jpg";

		bookImageService.mapImageToBook(book, null);

		verify(imageRepository).save(argThat(image ->
			"null.jpg".equals(image.getImageName()) &&
				defaultImageUrl.equals(image.getImageUrl())
		));
		verify(bookImageRepository).save(argThat(bookImage ->
			book.equals(bookImage.getBook()) &&
				"null.jpg".equals(bookImage.getImage().getImageName()) &&
				defaultImageUrl.equals(bookImage.getImage().getImageUrl())
		));
	}

	@Test
	void testMapImageToBook_WithEmptyUrl() {
		String emptyImageUrl = "";

		bookImageService.mapImageToBook(book, emptyImageUrl);

		verify(imageRepository).save(argThat(image ->
			"null.jpg".equals(image.getImageName()) &&
				"http://image.toast.com/aaaacuf/5ritang/books/null.jpg".equals(image.getImageUrl())
		));
		verify(bookImageRepository).save(argThat(bookImage ->
			book.equals(bookImage.getBook()) &&
				"null.jpg".equals(bookImage.getImage().getImageName()) &&
				"http://image.toast.com/aaaacuf/5ritang/books/null.jpg".equals(bookImage.getImage().getImageUrl())
		));
	}
}