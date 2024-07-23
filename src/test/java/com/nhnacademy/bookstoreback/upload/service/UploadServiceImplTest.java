package com.nhnacademy.bookstoreback.upload.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.nhnacademy.bookstoreback.upload.exception.ParserException;
import com.nhnacademy.bookstoreback.upload.service.impl.UploadServiceImpl;

class UploadServiceImplTest {

	@Mock
	private RestTemplate restTemplate;

	@InjectMocks
	private UploadServiceImpl uploadService;

	@Mock
	private MultipartFile multipartFile;

	@Value("${nhncloud.appkey}")
	private String appKey = "testAppKey";

	@Value("${nhncloud.secretkey}")
	private String secretKey = "testSecretKey";

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	// @Test
	// void testUploadSuccess() throws Exception {
	// 	when(multipartFile.getContentType()).thenReturn("image/jpeg");
	// 	when(multipartFile.getOriginalFilename()).thenReturn("test.jpg");
	// 	when(multipartFile.getBytes()).thenReturn("file content".getBytes());
	//
	// 	Path tempFile = Files.createTempFile("test", ".jpg");
	// 	Files.write(tempFile, "file content".getBytes());
	//
	// 	HttpHeaders headers = new HttpHeaders();
	// 	headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
	// 	headers.add("Authorization", secretKey);
	//
	// 	ResponseEntity<String> responseEntity = new ResponseEntity<>(
	// 		"{\"file\":{\"url\":\"http://example.com/image.jpg\"}}", HttpStatus.OK);
	// 	when(restTemplate.exchange(anyString(), eq(HttpMethod.PUT), any(HttpEntity.class), eq(String.class)))
	// 		.thenReturn(responseEntity);
	//
	// 	String imageUrl = uploadService.upload(multipartFile, anyString());
	//
	// 	assertEquals("http://example.com/image.jpg", imageUrl);
	// }
	//
	// @Test
	// void testUploadWithInvalidFileExtension() {
	// 	when(multipartFile.getContentType()).thenReturn("application/pdf");
	//
	// 	assertThrows(FileExtensionException.class, () -> uploadService.upload(multipartFile, anyString()));
	// }
	//
	// @Test
	// void testUploadFailure() throws Exception {
	// 	when(multipartFile.getContentType()).thenReturn("image/jpeg");
	// 	when(multipartFile.getOriginalFilename()).thenReturn("test.jpg");
	// 	when(multipartFile.getBytes()).thenReturn("file content".getBytes());
	//
	// 	Path tempFile = Files.createTempFile("test", ".jpg");
	// 	Files.write(tempFile, "file content".getBytes());
	//
	// 	HttpHeaders headers = new HttpHeaders();
	// 	headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
	// 	headers.add("Authorization", secretKey);
	//
	// 	ResponseEntity<String> responseEntity = new ResponseEntity<>("Internal Server Error",
	// 		HttpStatus.INTERNAL_SERVER_ERROR);
	// 	when(restTemplate.exchange(anyString(), eq(HttpMethod.PUT), any(HttpEntity.class), eq(String.class)))
	// 		.thenReturn(responseEntity);
	//
	// 	assertThrows(FileUploadException.class, () -> uploadService.upload(multipartFile, anyString()));
	// }

	@Test
	void testExtractImageUrlFromResponse() {
		String responseBody = "{\"file\":{\"url\":\"http://example.com/image.jpg\"}}";
		String imageUrl = uploadService.extractImageUrlFromResponse(responseBody);
		assertEquals("http://example.com/image.jpg", imageUrl);
	}

	@Test
	void testExtractImageUrlFromResponseWithInvalidJson() {
		String invalidResponseBody = "invalid json";
		assertThrows(ParserException.class, () -> uploadService.extractImageUrlFromResponse(invalidResponseBody));
	}
}