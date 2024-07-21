package com.nhnacademy.bookstoreback.wishlist.domain.dto.response;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import com.nhnacademy.bookstoreback.author.domain.entity.Author;
import com.nhnacademy.bookstoreback.book.domain.entity.Book;
import com.nhnacademy.bookstoreback.book.domain.entity.BookImage;
import com.nhnacademy.bookstoreback.image.domain.entity.Image;
import com.nhnacademy.bookstoreback.publisher.domain.entity.Publisher;
import com.nhnacademy.bookstoreback.wishlist.domain.entity.WishList;

class WishListResponseTest {

	@Test
	void testFromEntity() {
		Image image = mock(Image.class);
		when(image.getImageUrl()).thenReturn("http://example.com/image.jpg");

		BookImage bookImage = mock(BookImage.class);
		when(bookImage.getImage()).thenReturn(image);

		Book book = mock(Book.class);
		when(book.getBookId()).thenReturn(1L);
		when(book.getBookTitle()).thenReturn("Test Book");
		when(book.getAuthor()).thenReturn(mock(Author.class));
		when(book.getPublisher()).thenReturn(mock(Publisher.class));
		when(book.getBookSalePrice()).thenReturn(BigDecimal.valueOf(19.99));
		when(book.getBookSalePercent()).thenReturn(BigDecimal.valueOf(15.00));

		Author author = mock(Author.class);
		when(author.getAuthorName()).thenReturn("Test Author");
		when(book.getAuthor()).thenReturn(author);

		Publisher publisher = mock(Publisher.class);
		when(publisher.getPublisherName()).thenReturn("Test Publisher");
		when(book.getPublisher()).thenReturn(publisher);

		WishList wishList = mock(WishList.class);
		when(wishList.getWishListId()).thenReturn(1L);
		when(wishList.getBook()).thenReturn(book);
		when(book.getBookImages()).thenReturn(Collections.singletonList(bookImage));

		GetWishListResponse response = GetWishListResponse.fromEntity(wishList);
		
		assertEquals(1L, response.wishListId());
		assertEquals(1L, response.bookId());
		assertEquals("http://example.com/image.jpg", response.bookImageUrl());
		assertEquals("Test Book", response.bookTitle());
		assertEquals("Test Author", response.authorName());
		assertEquals("Test Publisher", response.publisherName());
		assertEquals(BigDecimal.valueOf(19.99), response.bookSalePrice());
		assertEquals(BigDecimal.valueOf(15.00), response.bookSalePercent());
	}
}