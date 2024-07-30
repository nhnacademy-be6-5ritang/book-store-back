package com.nhnacademy.bookstoreback.order;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.nhnacademy.bookstoreback.book.domain.entity.Book;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetListWrappingResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetWrappingResponse;
import com.nhnacademy.bookstoreback.order.domain.entity.BookOrder;
import com.nhnacademy.bookstoreback.order.domain.entity.Order;
import com.nhnacademy.bookstoreback.order.domain.entity.PaperType;
import com.nhnacademy.bookstoreback.order.domain.entity.WrappingPaper;
import com.nhnacademy.bookstoreback.order.repository.BookOrderRepository;
import com.nhnacademy.bookstoreback.order.repository.PaperTypeRepository;
import com.nhnacademy.bookstoreback.order.repository.WrappingPaperRepository;
import com.nhnacademy.bookstoreback.order.service.impl.WrappingPaperServiceImpl;

class WrappingPaperServiceImplTest {

	@InjectMocks
	private WrappingPaperServiceImpl wrappingPaperService;

	@Mock
	private WrappingPaperRepository wrappingPaperRepository;

	@Mock
	private PaperTypeRepository paperTypeRepository;

	@Mock
	private BookOrderRepository bookOrderRepository;

	private Order order;

	private Book book;

	@BeforeEach
	void setup() throws
		NoSuchMethodException,
		InvocationTargetException,
		InstantiationException,
		IllegalAccessException {
		MockitoAnnotations.openMocks(this);
		// 리플렉션을 사용하여 기본 생성자를 찾습니다.
		Constructor<Order> constructor = Order.class.getDeclaredConstructor();
		constructor.setAccessible(true);
		order = constructor.newInstance();

		Constructor<Book> bookConstructor = Book.class.getDeclaredConstructor();
		bookConstructor.setAccessible(true);
		book = bookConstructor.newInstance();
	}

	@Test
	void testCreateWrappingPapers_WithQuantity() {
		Long paperId = 1L;
		Long bookOrderId = 2L;
		Integer quantity = 10;

		PaperType paperType = new PaperType("Paper Name", "Paper Content", BigDecimal.TEN);
		BookOrder bookOrder = new BookOrder(5, book, order); // Initialize as needed
		WrappingPaper wrappingPaper = WrappingPaper.toEntity(bookOrder, paperType, quantity);

		when(paperTypeRepository.findById(paperId)).thenReturn(Optional.of(paperType));
		when(bookOrderRepository.getReferenceById(bookOrderId)).thenReturn(bookOrder);
		when(wrappingPaperRepository.save(any(WrappingPaper.class))).thenReturn(wrappingPaper);

		GetWrappingResponse response = wrappingPaperService.createWrappingPapers(paperId, bookOrderId, quantity);

		assertThat(response).isNotNull();
		assertThat(response.wrappingId()).isEqualTo(wrappingPaper.getWrappingPaperId());
		assertThat(response.name()).isEqualTo(paperType.getPaperName());
		assertThat(response.price()).isEqualTo(paperType.getPaperPrice());
		assertThat(response.quantity()).isEqualTo(quantity);
		verify(paperTypeRepository).findById(paperId);
		verify(bookOrderRepository).getReferenceById(bookOrderId);
		verify(wrappingPaperRepository).save(any(WrappingPaper.class));
	}

	@Test
	void testCreateWrappingPapers_WithoutQuantity() {
		Long paperId = 1L;
		Long bookOrderId = 2L;

		PaperType paperType = new PaperType("Paper Name", "Paper Content", BigDecimal.TEN);
		BookOrder bookOrder = new BookOrder(5, book, order); // Initialize as needed
		WrappingPaper wrappingPaper = WrappingPaper.toEntity(bookOrder, paperType, null);

		when(paperTypeRepository.findById(paperId)).thenReturn(Optional.of(paperType));
		when(bookOrderRepository.getReferenceById(bookOrderId)).thenReturn(bookOrder);
		when(wrappingPaperRepository.save(any(WrappingPaper.class))).thenReturn(wrappingPaper);

		GetWrappingResponse response = wrappingPaperService.createWrappingPapers(paperId, bookOrderId, null);

		assertThat(response).isNotNull();
		assertThat(response.wrappingId()).isEqualTo(wrappingPaper.getWrappingPaperId());
		assertThat(response.name()).isEqualTo(paperType.getPaperName());
		assertThat(response.price()).isEqualTo(paperType.getPaperPrice());
		assertThat(response.quantity()).isNull();
		verify(paperTypeRepository).findById(paperId);
		verify(bookOrderRepository).getReferenceById(bookOrderId);
		verify(wrappingPaperRepository).save(any(WrappingPaper.class));
	}

	@Test
	void testDeleteWrappingPapers() {
		Long id = 1L;

		wrappingPaperService.deleteWrappingPapers(id);

		verify(wrappingPaperRepository).deleteById(id);
	}

	@Test
	void testGetWrappingPaperByOrderListId() {
		Long orderListId = 1L;
		WrappingPaper wrappingPaper = WrappingPaper.toEntity(new BookOrder(5, book, order),
			new PaperType("Name", "Content", BigDecimal.TEN), 5);
		when(wrappingPaperRepository.findAllByBookOrder_OrderListId(orderListId))
			.thenReturn(Collections.singletonList(wrappingPaper));

		GetListWrappingResponse response = wrappingPaperService.getWrappingPaperByOrderListId(orderListId);

		assertThat(response).isNotNull();
		assertThat(response.wrapping()).hasSize(1);
		assertThat(response.wrapping().getFirst()).isEqualTo(GetWrappingResponse.from(wrappingPaper));
		verify(wrappingPaperRepository).findAllByBookOrder_OrderListId(orderListId);
	}
}
