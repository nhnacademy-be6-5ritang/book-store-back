package com.nhnacademy.bookstoreback.upload.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nhnacademy.bookstoreback.keymanager.property.ImageManagerProperty;
import com.nhnacademy.bookstoreback.keymanager.service.KeyManagerService;
import com.nhnacademy.bookstoreback.upload.exception.FileExtensionException;
import com.nhnacademy.bookstoreback.upload.exception.FileUploadException;
import com.nhnacademy.bookstoreback.upload.exception.ParserException;
import com.nhnacademy.bookstoreback.upload.service.impl.UploadServiceImpl;

@ExtendWith(MockitoExtension.class)
class UploadServiceImplTest {

	@InjectMocks
	private UploadServiceImpl uploadService;

	@Mock
	private RestTemplate restTemplate;

	@Mock
	private MultipartFile multipartFile;

	@Mock
	private KeyManagerService keyManagerService;

	@Mock
	private ImageManagerProperty imageManagerProperty;

	private String appKey = "testAppKey";
	private String secretKey = "testSecretKey";

	@BeforeEach
	void setUp() {
		uploadService = new UploadServiceImpl(restTemplate, keyManagerService, imageManagerProperty);
	}

	@Test
	void testUpload_Failure() throws Exception {
		String fileName = "test-image.jpg";
		byte[] fileBytes = "test image data".getBytes(StandardCharsets.UTF_8);

		when(multipartFile.getOriginalFilename()).thenReturn(fileName);
		when(multipartFile.getBytes()).thenReturn(fileBytes);
		when(multipartFile.getContentType()).thenReturn("image/jpeg");

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		headers.add("Authorization", secretKey);

		String url = "https://api-image.nhncloudservice.com/image/v2.0/appkeys/" + appKey +
			"/images?path=/5ritang/books/" + UUID.randomUUID() + "_" + fileName + "&overwrite=true";

		HttpEntity<byte[]> requestEntity = new HttpEntity<>(fileBytes, headers);
		ResponseEntity<String> response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);

		when(restTemplate.exchange(eq(url), eq(HttpMethod.PUT), eq(requestEntity), eq(String.class)))
			.thenReturn(response);

		assertThrows(FileUploadException.class, () -> uploadService.upload(multipartFile, "books"));
	}

	@Test
	void testExtractImageUrlFromResponse_Success() throws JsonProcessingException {
		String responseBody = "{\"file\": {\"url\": \"http://example.com/test-image.jpg\"}}";
		String expectedUrl = "http://example.com/test-image.jpg";

		String imageUrl = uploadService.extractImageUrlFromResponse(responseBody);

		assertEquals(expectedUrl, imageUrl);
	}

	@Test
	void testExtractImageUrlFromResponse_JsonProcessingException() throws JsonProcessingException {
		String invalidJson = "{file: {url: \"http://example.com/test-image.jpg\"}";

		assertThrows(ParserException.class, () -> uploadService.extractImageUrlFromResponse(invalidJson));
	}

	@Test
	void testCheckFileExtension_ValidExtension() {
		uploadService.checkFileExtension("image/jpeg");
		uploadService.checkFileExtension("image/png");
		uploadService.checkFileExtension("image/jpg");
	}

	@Test
	void testCheckFileExtension_InvalidExtension() {
		assertThrows(FileExtensionException.class, () -> uploadService.checkFileExtension("application/pdf"));
	}
}
