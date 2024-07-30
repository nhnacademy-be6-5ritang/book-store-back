package com.nhnacademy.bookstoreback.order.repository.impl;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.nhnacademy.bookstoreback.author.domain.entity.Author;
import com.nhnacademy.bookstoreback.book.domain.entity.Book;
import com.nhnacademy.bookstoreback.bookstatus.domain.entity.BookStatus;
import com.nhnacademy.bookstoreback.category.domain.entity.BookCategory;
import com.nhnacademy.bookstoreback.category.domain.entity.Category;
import com.nhnacademy.bookstoreback.config.QuerydslTestConfig;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetBookByOrderCouponResponse;
import com.nhnacademy.bookstoreback.order.domain.entity.BookOrder;
import com.nhnacademy.bookstoreback.order.domain.entity.Order;
import com.nhnacademy.bookstoreback.order.domain.entity.OrderStatus;
import com.nhnacademy.bookstoreback.publisher.domain.entity.Publisher;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

@DataJpaTest
@Import(QuerydslTestConfig.class) // QueryDSL 설정 파일 임포트
class CustomBookOrderRepositoryImplTest {

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private JPAQueryFactory jpaQueryFactory;

	private CustomBookOrderRepositoryImpl customBookOrderRepository;

	@BeforeEach
	void setUp() {
		customBookOrderRepository = new CustomBookOrderRepositoryImpl(jpaQueryFactory);

		// Create Categories
		Category parentCategory = Category.builder()
			.categoryName("ParentCategory")
			.build();
		entityManager.persist(parentCategory);

		Category scienceCategory = Category.builder()
			.categoryName("Science")
			.parentCategory(parentCategory)
			.build();
		entityManager.persist(scienceCategory);


		BookStatus bookStatus = BookStatus.builder().bookStatusName("Available").build();
		entityManager.persist(bookStatus);

		Author author = Author.builder().authorName("sam").build();
		entityManager.persist(author);

		Publisher publisher = Publisher.builder().publisherName("kim").build();
		entityManager.persist(publisher);

		// Create Book
		Book book = Book.builder()
			.bookTitle("Sample Book One")
			.bookDescription("Sample Book One")
			.bookPrice(BigDecimal.valueOf(10000))
			.bookQuantity(100)
			.bookStatus(bookStatus)
			.author(author)
			.bookIsbn("1234")
			.bookPublishDate(Date.from(Instant.now()))
			.bookSalePercent(BigDecimal.valueOf(0.8))
			.bookSalePrice(BigDecimal.valueOf(10000))
			.publisher(publisher)
			.build();

		entityManager.persist(book);


		OrderStatus orderStatus=OrderStatus.builder().orderStatusName("주문전").build();
		entityManager.persist(orderStatus);
		// Create Order
		Order order = Order.builder()
			.orderPayerName("ki-hoon")
			.orderPayerEmail("kihoon@naver.com")
			.orderPayerNumber("12345")
			.orderPayerAddress("광주광역시 북구 자미로 23번길")
			.orderPrice(BigDecimal.valueOf(10000))
			.orderPointSale(BigDecimal.valueOf(2000))
			.orderCouponSale(BigDecimal.valueOf(2000))
			.orderInfoId("order-12345")
			.orderDate(LocalDateTime.now())
			.orderStatus(orderStatus)
			.build();
		entityManager.persist(order);


		// Create BookOrder
		BookOrder bookOrder = BookOrder.builder()
			.bookQuantity(1)
			.book(book)
			.order(order)
			.build();
		entityManager.persist(bookOrder);

		// Create BookCategory

		BookCategory science = new BookCategory(book, scienceCategory);
		BookCategory parent = new BookCategory(book, parentCategory);

		entityManager.persist(parent);
		entityManager.persist(science);



		// Flush and clear to ensure persistence context is updated
		entityManager.flush();
		entityManager.clear();
	}

	@Test
	void testFindBooksByOrderListId() {
		// Given
		Long orderListId = 1L; // Assume the ID of the BookOrder we created is 1

		// When
		GetBookByOrderCouponResponse response = customBookOrderRepository.findBooksByOrderListId(orderListId);




		// Then
		assertThat(response).isNotNull();
		assertThat(response.bookId()).isEqualTo(1L); // bookId should be 1

		// Convert BigDecimal to String and compare
		assertThat(response.bookPrice().toPlainString()).isEqualTo("10000.00");

		// Category ID comparisons
		assertThat(response.categoryId()).containsExactly(1L, 2L);
	}


}