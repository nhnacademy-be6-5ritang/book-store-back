package com.nhnacademy.bookstoreback.wishlist.domain.entity;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Constructor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.nhnacademy.bookstoreback.book.domain.entity.Book;
import com.nhnacademy.bookstoreback.user.domain.entity.User;

class WishListEntityTest {

	private Book mockBook;
	private User mockUser;

	@BeforeEach
	void setUp() {
		mockBook = Mockito.mock(Book.class);
		mockUser = Mockito.mock(User.class);

		Mockito.when(mockBook.getBookId()).thenReturn(1L);
		Mockito.when(mockUser.getId()).thenReturn(1L);
	}

	@Test
	void testNoArgsConstructorIsProtected() {
		try {
			Constructor<WishList> constructor = WishList.class.getDeclaredConstructor();
			assertTrue(java.lang.reflect.Modifier.isProtected(constructor.getModifiers()),
				"Default constructor is not protected");
		} catch (NoSuchMethodException e) {
			fail("No default constructor found");
		}
		new WishList();
	}

	@Test
	void testWishListCreation() {
		WishList wishList = new WishList(mockBook, mockUser);

		assertNotNull(wishList);
		assertEquals(mockBook, wishList.getBook());
		assertEquals(mockUser, wishList.getUser());
		assertNull(wishList.getWishListId(), "wishListId should be null upon creation");
	}
}
