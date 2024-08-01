package com.nhnacademy.bookstoreback.upload.service.impl;

import java.util.UUID;

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
import com.nhnacademy.bookstoreback.keymanager.property.ImageManagerProperty;
import com.nhnacademy.bookstoreback.keymanager.service.KeyManagerService;
import com.nhnacademy.bookstoreback.upload.exception.FileExtensionException;
import com.nhnacademy.bookstoreback.upload.exception.FileUploadException;
import com.nhnacademy.bookstoreback.upload.exception.ParserException;
import com.nhnacademy.bookstoreback.upload.service.UploadService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UploadServiceImpl implements UploadService {
	private final RestTemplate restTemplate;
	private final KeyManagerService keyManagerService;
	private final ImageManagerProperty imageManagerProperty;

	public String upload(MultipartFile file, String folderName) {
		// 파일 확장자 검사
		checkFileExtension(file.getContentType());

		try {
			String fileName = String.format("%s_%s", UUID.randomUUID(), file.getOriginalFilename());

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			headers.add("Authorization", keyManagerService.getSecret(imageManagerProperty.getSecretKey()));

			String url = "https://api-image.nhncloudservice.com/image/v2.0/appkeys/" + keyManagerService.getSecret(
				imageManagerProperty.getAppKey()) + "/images?path=/5ritang/"
				+ folderName + "/" + fileName + "&overwrite=true";

			log.info("Uploading to URL: {}", url);

			HttpEntity<byte[]> requestEntity = new HttpEntity<>(file.getBytes(), headers);

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

	public String extractImageUrlFromResponse(String responseBody) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			JsonNode rootNode = mapper.readTree(responseBody);
			return rootNode.path("file").path("url").asText();
		} catch (JsonProcessingException e) {
			throw new ParserException();
		}
	}

	public void checkFileExtension(String fileContentType) {
		String[] imageExtensions = {"jpg", "jpeg", "png"};

		for (String extension : imageExtensions) {
			if (fileContentType.endsWith(extension)) {
				return;
			}
		}
		throw new FileExtensionException();
	}

}
