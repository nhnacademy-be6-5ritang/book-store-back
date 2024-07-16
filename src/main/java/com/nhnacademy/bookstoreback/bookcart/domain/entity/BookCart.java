package com.nhnacademy.bookstoreback.bookcart.domain.entity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import com.nhnacademy.bookstoreback.bookcart.domain.dto.request.CreateBookCartRequest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author 이경헌
 * Redis 에 저장되는 도서 장바구니 엔티티입니다.
 */
@RedisHash(value = "bookCarts")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookCart {
	@Id
	@NotBlank
	@Size(min = 1, max = 36)
	private String cartId;
	private List<Book> books = new ArrayList<>();

	@Builder
	public BookCart(String cartId, List<Book> books) {
		this.cartId = cartId;
		this.books = books;
	}

	public static BookCart toEntity(BookCart bookCart, CreateBookCartRequest request) {
		Book book = new Book(request.bookId(), request.bookQuantity());
		List<Book> books = bookCart.getBooks();
		books.add(book);
		return BookCart.builder()
			.cartId(bookCart.cartId)
			.books(books)
			.build();
	}

	public void updateBookQuantity(Long bookId, Integer bookQuantity) {
		Optional<Book> bookOptional = books.stream()
			.filter(book -> book.getBookId().equals(bookId))
			.findFirst();

		bookOptional.ifPresent(book -> book.updateBookQuantity(bookQuantity));
	}

	public void removeBook(Long bookId) {
		Iterator<Book> iterator = this.books.iterator();
		while (iterator.hasNext()) {
			Book book = iterator.next();
			if (book.getBookId().equals(bookId)) {
				iterator.remove();
				break;
			}
		}
	}
}
