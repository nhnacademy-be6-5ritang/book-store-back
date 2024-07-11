package com.nhnacademy.bookstoreback.reviewimage.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api")
public class ImageUploadController {

	@Value("${nhncloud.appkey}")
	private String appKey;

	@Value("${nhncloud.secretkey}")
	private String secretKey;

	private final RestTemplate restTemplate = new RestTemplate();

	@PostMapping("/upload-image")  // @PutMapping에서 @PostMapping으로 변경
	public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) {
		try {
			String fileName = file.getOriginalFilename();
			Path tempFile = Files.createTempFile(UUID.randomUUID().toString(), file.getOriginalFilename());
			Files.write(tempFile, file.getBytes());

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			headers.add("Authorization", secretKey);

			String url = "https://api-image.nhncloudservice.com/image/v2.0/appkeys/" + appKey + "/images?path=/5ritang/reviews/" + fileName + "&overwrite=true";
			System.out.println("Uploading to URL: " + url);

			HttpEntity<byte[]> requestEntity = new HttpEntity<>(Files.readAllBytes(tempFile), headers);

			ResponseEntity<String> response = restTemplate.exchange(
				url,
				HttpMethod.PUT,
				requestEntity,
				String.class
			);

			System.out.println("Response Status: " + response.getStatusCode());
			System.out.println("Response Body: " + response.getBody());

			if (response.getStatusCode().is2xxSuccessful()) {
				String imageUrl = extractImageUrlFromResponse(response.getBody());
				return ResponseEntity.ok(Collections.singletonMap("imageUrl", imageUrl));
			} else {
				return ResponseEntity.status(response.getStatusCode()).body(Collections.singletonMap("message", "Image upload failed"));
			}
		} catch (HttpClientErrorException e) {
			e.printStackTrace();
			return ResponseEntity.status(e.getStatusCode()).body(Collections.singletonMap("message", "HTTP Error: " + e.getMessage()));
		} catch (IOException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("message", "File processing error: " + e.getMessage()));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("message", "Unexpected error: " + e.getMessage()));
		}
	}

	private String extractImageUrlFromResponse(String responseBody) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			JsonNode rootNode = mapper.readTree(responseBody);
			return rootNode.path("file").path("url").asText();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new RuntimeException("Failed to parse response body", e);
		}
	}
}
