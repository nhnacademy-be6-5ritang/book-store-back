package com.nhnacademy.bookstoreback.upload.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookstoreback.upload.exception.FileExtensionException;
import com.nhnacademy.bookstoreback.upload.exception.FileUploadException;
import com.nhnacademy.bookstoreback.upload.exception.ParserException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UploadService {
	private final RestTemplate restTemplate;

	@Value("${nhncloud.appkey}")
	private String appKey;

	@Value("${nhncloud.secretkey}")
	private String secretKey;

	public String upload(MultipartFile file) {
		// 파일 확장자 검사
		checkFileExtension(file.getContentType());

		try {
			String fileName = String.format("%s_%s", UUID.randomUUID(), file.getOriginalFilename());
			Path tempFile = Files.createTempFile(UUID.randomUUID().toString(), file.getOriginalFilename());
			Files.write(tempFile, file.getBytes());

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			headers.add("Authorization", secretKey);

			String url =
				"https://api-image.nhncloudservice.com/image/v2.0/appkeys/" + appKey + "/images?path=/5ritang/reviews/"
					+ fileName + "&overwrite=true";

			log.info("Uploading to URL: {}", url);

			HttpEntity<byte[]> requestEntity = new HttpEntity<>(Files.readAllBytes(tempFile), headers);

			ResponseEntity<String> response = restTemplate.exchange(
				url,
				HttpMethod.PUT,
				requestEntity,
				String.class
			);

			log.info("Response Status:{}, Response Body:{}", response.getStatusCode(), response.getBody());

			if (response.getStatusCode().is2xxSuccessful()) {
				return extractImageUrlFromResponse(response.getBody());
			} else {
				throw new FileUploadException(null);
			}
		} catch (Exception e) {
			throw new FileUploadException(e.getMessage());
		}
	}

	private String extractImageUrlFromResponse(String responseBody) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			JsonNode rootNode = mapper.readTree(responseBody);
			return rootNode.path("file").path("url").asText();
		} catch (JsonProcessingException e) {
			throw new ParserException();
		}
	}

	private void checkFileExtension(String fileContentType) {
		String[] imageExtensions = {"jpg", "jpeg", "png"};

		for (String extension : imageExtensions) {
			if (fileContentType.endsWith(extension)) {
				return;
			}
		}
		throw new FileExtensionException();
	}

}
