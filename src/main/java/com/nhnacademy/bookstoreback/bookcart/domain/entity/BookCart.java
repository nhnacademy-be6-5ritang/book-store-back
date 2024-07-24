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
	private List<BookBundle> bookBundles = new ArrayList<>();

	@Builder
	public BookCart(String cartId, List<BookBundle> bookBundles) {
		this.cartId = cartId;
		this.bookBundles = bookBundles;
	}

	public static BookCart toEntity(BookCart bookCart, CreateBookCartRequest request) {
		BookBundle bookBundle = new BookBundle(request.bookId(), request.bookQuantity());
		List<BookBundle> bookBundles = bookCart.getBookBundles();
		bookBundles.add(bookBundle);
		return BookCart.builder()
			.cartId(bookCart.cartId)
			.bookBundles(bookBundles)
			.build();
	}

	public void updateBookQuantity(Long bookId, Integer bookQuantity) {
		Optional<BookBundle> bookOptional = bookBundles.stream()
			.filter(bookBundle -> bookBundle.getBookId().equals(bookId))
			.findFirst();

		bookOptional.ifPresent(bookBundle -> bookBundle.updateBookQuantity(bookQuantity));
	}

	public void removeBook(Long bookId) {
		Iterator<BookBundle> iterator = this.bookBundles.iterator();
		while (iterator.hasNext()) {
			BookBundle bookBundle = iterator.next();
			if (bookBundle.getBookId().equals(bookId)) {
				iterator.remove();
				break;
			}
		}
	}

	public void removeAll() {
		bookBundles = new ArrayList<>();
	}
}
