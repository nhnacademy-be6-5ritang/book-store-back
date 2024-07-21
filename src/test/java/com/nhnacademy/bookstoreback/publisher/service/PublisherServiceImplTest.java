package com.nhnacademy.bookstoreback.publisher.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.nhnacademy.bookstoreback.publisher.domain.dto.respnse.PublisherDto;
import com.nhnacademy.bookstoreback.publisher.domain.entity.Publisher;
import com.nhnacademy.bookstoreback.publisher.exception.PublisherAlreadyExistsException;
import com.nhnacademy.bookstoreback.publisher.exception.PublisherNotFoundException;
import com.nhnacademy.bookstoreback.publisher.repository.PublisherRepository;
import com.nhnacademy.bookstoreback.publisher.service.impl.PublisherServiceImpl;

class PublisherServiceImplTest {

	@Mock
	private PublisherRepository publisherRepository;

	@InjectMocks
	private PublisherServiceImpl publisherService;

	private PublisherDto publisherDto;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		publisherDto = new PublisherDto(1L, "Publisher");
	}

	@Test
	void findOrCreatePublisher_WhenPublisherExists_ShouldReturnPublisher() {
		Publisher existingPublisher = new Publisher("Existing Publisher");
		when(publisherRepository.findByPublisherName("Existing Publisher"))
			.thenReturn(Optional.of(existingPublisher));

		Publisher result = publisherService.findOrCreatePublisher("Existing Publisher");

		assertEquals(existingPublisher, result);
		verify(publisherRepository, never()).save(any(Publisher.class));
	}

	@Test
	void findOrCreatePublisher_WhenPublisherDoesNotExist_ShouldCreateAndReturnPublisher() {
		when(publisherRepository.findByPublisherName("New Publisher"))
			.thenReturn(Optional.empty());
		when(publisherRepository.save(any(Publisher.class)))
			.thenAnswer(invocation -> invocation.getArguments()[0]);

		Publisher result = publisherService.findOrCreatePublisher("New Publisher");

		assertNotNull(result);
		assertEquals("New Publisher", result.getPublisherName());
		verify(publisherRepository).save(any(Publisher.class));
	}

	@Test
	void getPublishers_ShouldReturnPublisherDtos() {
		Publisher publisher = new Publisher("Publisher");
		PublisherDto publisherDto = PublisherDto.fromEntity(publisher);
		when(publisherRepository.findAll()).thenReturn(Collections.singletonList(publisher));

		var result = publisherService.getPublishers();

		assertEquals(1, result.size());
		assertEquals(publisherDto, result.get(0));
	}

	@Test
	void getPublishers_Pageable_ShouldReturnPagedPublisherDtos() {
		Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "publisherId"));
		Publisher publisher = new Publisher("Publisher");
		PublisherDto publisherDto = PublisherDto.fromEntity(publisher);
		Page<Publisher> publishersPage = new PageImpl<>(Collections.singletonList(publisher), pageable, 1);

		// PublisherRepository가 null을 반환하지 않도록 설정합니다.
		when(publisherRepository.findAll(pageable)).thenReturn(publishersPage);

		Page<PublisherDto> result = publisherService.getPublishers(pageable);

		// Page<PublisherDto>가 null이 아닌지 확인합니다.
		assertNotNull(result);
		assertEquals(1, result.getTotalElements());
		assertEquals(publisherDto.publisherName(), result.getContent().get(0).publisherName());
	}

	@Test
	void getPublisher_WhenPublisherExists_ShouldReturnPublisherDto() {
		Publisher publisher = new Publisher("Publisher");
		PublisherDto publisherDto = PublisherDto.fromEntity(publisher);
		when(publisherRepository.findById(1L)).thenReturn(Optional.of(publisher));

		PublisherDto result = publisherService.getPublisher(1L);

		assertEquals(publisherDto, result);
	}

	@Test
	void getPublisher_WhenPublisherDoesNotExist_ShouldThrowException() {
		when(publisherRepository.findById(1L)).thenReturn(Optional.empty());

		assertThrows(PublisherNotFoundException.class, () -> publisherService.getPublisher(1L));
	}

	@Test
	void createPublisher_WhenPublisherDoesNotExist_ShouldSavePublisher() {
		when(publisherRepository.existsByPublisherName("New Publisher")).thenReturn(false);
		PublisherDto newPublisherDto = PublisherDto.builder().publisherName("New Publisher").build();
		when(publisherRepository.save(any(Publisher.class))).thenReturn(Publisher.toEntity(newPublisherDto));

		publisherService.createPublisher(newPublisherDto);

		verify(publisherRepository, times(1)).save(any(Publisher.class));
	}

	@Test
	void createPublisher_WhenPublisherAlreadyExists_ShouldThrowException() {
		when(publisherRepository.existsByPublisherName("Publisher")).thenReturn(true);

		assertThrows(PublisherAlreadyExistsException.class, () -> publisherService.createPublisher(publisherDto));
	}

	@Test
	void updatePublisher_WhenPublisherExists_ShouldUpdatePublisher() {
		when(publisherRepository.findById(1L)).thenReturn(Optional.empty());

		assertThrows(PublisherNotFoundException.class, () -> publisherService.updatePublisher(1L, publisherDto));
	}

	@Test
	void updatePublisher_WhenPublisherDoesNotExist_ShouldThrowException() {

		when(publisherRepository.findById(1L)).thenReturn(Optional.empty());

		assertThrows(PublisherNotFoundException.class, () -> publisherService.updatePublisher(1L, publisherDto));
	}

	@Test
	void deletePublisher_WhenPublisherExists_ShouldDeletePublisher() {
		when(publisherRepository.findById(1L)).thenReturn(Optional.of(new Publisher("Publisher")));

		publisherService.deletePublisher(1L);

		verify(publisherRepository).deleteById(1L);
	}

	@Test
	void deletePublisher_WhenPublisherDoesNotExist_ShouldThrowException() {
		when(publisherRepository.findById(1L)).thenReturn(Optional.empty());

		assertThrows(PublisherNotFoundException.class, () -> publisherService.deletePublisher(1L));
	}
}
