package com.nhnacademy.bookstoreback.image.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.nhnacademy.bookstoreback.book.domain.entity.Book;
import com.nhnacademy.bookstoreback.upload.service.UploadService;

class CloudImageServiceTest {

	@InjectMocks
	private CloudImageService cloudImageService;

	@Mock
	private RestTemplate restTemplate;

	@Mock
	private UploadService uploadService;

	private Book book;

	private final String apiUrl = "https://api.example.com/search?isbn=1234567890";
	private final HttpEntity<String> requestEntity = new HttpEntity<>(null);

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		book = Book.builder()
			.bookTitle("Test Book")
			.bookIsbn("1234567890")
			.build();
	}

	@Test
	void testUploadImageForBookByNaverApi_ApiFailure() {
		when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(Map.class)))
			.thenThrow(new HttpClientErrorException(HttpStatus.TOO_MANY_REQUESTS));

		String result = cloudImageService.uploadImageForBookByNaverApi(book);

		verify(restTemplate).exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(Map.class));
		assertNull(result);
	}

	@Test
	void testExecuteApiCall_Success() {
		Map<String, Object> apiResponse = Map.of("imageUrl", "http://example.com/image.jpg");
		ResponseEntity<Map> responseEntity = ResponseEntity.ok(apiResponse);

		when(restTemplate.exchange(eq(apiUrl), eq(HttpMethod.GET), eq(requestEntity), eq(Map.class)))
			.thenReturn(responseEntity);

		ResponseEntity<Map> result = cloudImageService.executeApiCall(apiUrl, requestEntity);
		assertNotNull(result);
		assertNotNull(result.getBody());
		assertEquals("http://example.com/image.jpg", result.getBody().get("imageUrl"));
	}

	@Test
	void testExecuteApiCall_Failure() {
		// Simulate a different exception
		when(restTemplate.exchange(eq(apiUrl), eq(HttpMethod.GET), eq(requestEntity), eq(Map.class)))
			.thenThrow(new RuntimeException("Generic error"));

		ResponseEntity<Map> result = cloudImageService.executeApiCall(apiUrl, requestEntity);
		assertNull(result);
	}

}