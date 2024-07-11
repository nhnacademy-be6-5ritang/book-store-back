package com.nhnacademy.bookstoreback.reviewimage.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class CloudStorageService {

	@Value("${nhncloud.appkey}")
	private String appKey;

	@Value("${naver.client.id}")
	private String secretKey;

	private static final String CLOUD_PATH = "/5ritang/reviews";

	private final String API_URL = "https://api-image.nhncloudservice.com/image/v2.0/appkeys/";

	public String uploadFile(MultipartFile file) throws IOException {
		RestTemplate restTemplate = new RestTemplate();

		String filename = UUID.randomUUID().toString() + getFileExtension(file.getOriginalFilename());
		String fullPath = CLOUD_PATH + "/" + filename;

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		headers.set("Authorization", secretKey);

		HttpEntity<byte[]> requestEntity = new HttpEntity<>(file.getBytes(), headers);

		String url = API_URL + appKey + "/images?path=" + fullPath + "&overwrite=true";

		ResponseEntity<String> response = restTemplate.exchange(
			url,
			HttpMethod.PUT,
			requestEntity,
			String.class
		);

		if (response.getStatusCode() == HttpStatus.OK) {
			// JSON 응답에서 url 필드를 추출하는 로직이 필요합니다.
			// 여기서는 간단히 전체 응답을 반환합니다.
			return response.getBody();
		} else {
			throw new RuntimeException("Failed to upload image: " + response.getBody());
		}
	}

	private String getFileExtension(String filename) {
		return filename.substring(filename.lastIndexOf("."));
	}
}