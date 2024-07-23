package com.nhnacademy.bookstoreback.bookstatus.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.nhnacademy.bookstoreback.bookstatus.domain.dto.respnse.BookStatusDto;
import com.nhnacademy.bookstoreback.bookstatus.domain.entity.BookStatus;
import com.nhnacademy.bookstoreback.bookstatus.exception.BookStatusAlreadyExistsException;
import com.nhnacademy.bookstoreback.bookstatus.exception.BookStatusNotFoundException;
import com.nhnacademy.bookstoreback.bookstatus.repository.BookStatusRepository;

@ExtendWith(MockitoExtension.class)
class BookStatusServiceImplTest {

	@Mock
	private BookStatusRepository bookStatusRepository;

	@InjectMocks
	private BookStatusServiceImpl bookStatusService;

	private BookStatus bookStatus;

	@BeforeEach
	void setUp() {
		bookStatus = new BookStatus(1L, "ON_SALE");
	}

	@Test
	void testGetBookStatuses() {
		when(bookStatusRepository.findAll()).thenReturn(Arrays.asList(bookStatus));

		List<BookStatusDto> result = bookStatusService.getBookStatuses();
		assertEquals(1, result.size());
		assertEquals("ON_SALE", result.get(0).bookStatusName());
	}

	@Test
	void testGetBookStatus() {
		when(bookStatusRepository.findById(anyLong())).thenReturn(Optional.of(bookStatus));

		BookStatusDto result = bookStatusService.getBookStatus(1L);
		assertEquals("ON_SALE", result.bookStatusName());
	}

	@Test
	void testGetBookStatus_NotFound() {
		when(bookStatusRepository.findById(anyLong())).thenReturn(Optional.empty());

		assertThrows(BookStatusNotFoundException.class, () -> bookStatusService.getBookStatus(1L));
	}

	@Test
	void testCreateBookStatus() {
		BookStatusDto request = new BookStatusDto(null, "ON_SALE");
		when(bookStatusRepository.existsByBookStatusName(anyString())).thenReturn(false);
		when(bookStatusRepository.save(any(BookStatus.class))).thenReturn(bookStatus);

		bookStatusService.createBookStatus(request);
		verify(bookStatusRepository, times(1)).save(any(BookStatus.class));
	}

	@Test
	void testCreateBookStatus_AlreadyExists() {
		BookStatusDto request = new BookStatusDto(null, "ON_SALE");
		when(bookStatusRepository.existsByBookStatusName(anyString())).thenReturn(true);

		assertThrows(BookStatusAlreadyExistsException.class, () -> bookStatusService.createBookStatus(request));
	}

	@Test
	void testUpdateBookStatus() {
		BookStatusDto request = new BookStatusDto(null, "UPDATED_STATUS");
		when(bookStatusRepository.findById(anyLong())).thenReturn(Optional.of(bookStatus));
		when(bookStatusRepository.existsByBookStatusName(anyString())).thenReturn(false);

		bookStatusService.updateBookStatus(1L, request);
		assertEquals("UPDATED_STATUS", bookStatus.getBookStatusName());
	}

	@Test
	void testUpdateBookStatus_NotFound() {
		BookStatusDto request = new BookStatusDto(null, "UPDATED_STATUS");
		when(bookStatusRepository.findById(anyLong())).thenReturn(Optional.empty());

		assertThrows(BookStatusNotFoundException.class, () -> bookStatusService.updateBookStatus(1L, request));
	}

	@Test
	void testUpdateBookStatus_AlreadyExists() {
		BookStatusDto request = new BookStatusDto(null, "UPDATED_STATUS");
		when(bookStatusRepository.findById(anyLong())).thenReturn(Optional.of(bookStatus));
		when(bookStatusRepository.existsByBookStatusName(anyString())).thenReturn(true);

		assertThrows(BookStatusAlreadyExistsException.class, () -> bookStatusService.updateBookStatus(1L, request));
	}

	@Test
	void testDeleteBookStatus() {
		when(bookStatusRepository.findById(anyLong())).thenReturn(Optional.of(bookStatus));
		doNothing().when(bookStatusRepository).deleteById(anyLong());

		bookStatusService.deleteBookStatus(1L);
		verify(bookStatusRepository, times(1)).deleteById(anyLong());
	}

	@Test
	void testDeleteBookStatus_NotFound() {
		when(bookStatusRepository.findById(anyLong())).thenReturn(Optional.empty());

		assertThrows(BookStatusNotFoundException.class, () -> bookStatusService.deleteBookStatus(1L));
	}

}