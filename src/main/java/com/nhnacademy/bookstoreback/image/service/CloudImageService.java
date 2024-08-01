package com.nhnacademy.bookstoreback.image.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.nhnacademy.bookstoreback.book.domain.entity.Book;
import com.nhnacademy.bookstoreback.global.util.CustomMultipartFile;
import com.nhnacademy.bookstoreback.keymanager.property.NaverApiProperty;
import com.nhnacademy.bookstoreback.keymanager.service.KeyManagerService;
import com.nhnacademy.bookstoreback.upload.service.UploadService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 김기욱
 * 클라우드 이미지 서비스를 제공하는 클래스입니다.
 * 네이버 API 를 통해 책의 표지 이미지를 가져와서 업로드합니다.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CloudImageService {
	private static final int MAX_RETRY_COUNT = 3; // 최대 재시도 횟수
	private static final int RETRY_DELAY_MS = 2000; // 재시도 간 대기 시간 (밀리초)

	private final RestTemplate restTemplate;
	private final UploadService uploadService;
	private final KeyManagerService keyManagerService;
	private final NaverApiProperty naverApiProperty;

	/**
	 * 네이버 API 를 통해 책의 표지 이미지를 가져와서 업로드합니다.
	 *
	 * @param book 이미지가 업로드될 책 엔티티
	 * @return 업로드된 이미지의 URL, 업로드 실패 시 {@code null}
	 */
	public String uploadImageForBookByNaverApi(Book book) {
		String isbn = book.getBookIsbn();
		String apiUrl = "https://openapi.naver.com/v1/search/book.json?query=" + isbn;

		HttpHeaders headers = new HttpHeaders();
		headers.set("X-Naver-Client-Id", keyManagerService.getSecret(naverApiProperty.getId()));
		headers.set("X-Naver-Client-Secret", keyManagerService.getSecret(naverApiProperty.getSecret()));

		HttpEntity<String> requestEntity = new HttpEntity<>(headers);

		ResponseEntity<Map> response = executeApiCall(apiUrl, requestEntity);
		if (response == null) {
			log.error("ISBN {}에 대한 API 응답을 받지 못했습니다.", isbn);
			return null;
		}

		Map<String, Object> body = response.getBody();
		List<Object> items = (List<Object>)body.get("items");
		if (items == null || items.isEmpty()) {
			log.error("ISBN {}에 대한 API 응답에서 항목을 찾을 수 없습니다.", isbn);
			return null;
		}

		Map<String, Object> item = (Map<String, Object>)items.get(0);
		String coverUrl = (String)item.get("image");

		return downloadAndUploadImage(book, coverUrl);
	}

	/**
	 * 네이버 API 호출을 실행합니다.
	 *
	 * @param apiUrl         호출할 API 의 URL
	 * @param requestEntity  요청에 사용할 HTTP 엔티티
	 * @return API 호출의 응답, 실패 시 {@code null}
	 */
	public ResponseEntity<Map> executeApiCall(String apiUrl, HttpEntity<String> requestEntity) {
		int attempt = 0;
		while (attempt < MAX_RETRY_COUNT) {
			try {
				return restTemplate.exchange(apiUrl, HttpMethod.GET, requestEntity, Map.class);
			} catch (HttpClientErrorException.TooManyRequests e) {
				attempt++;
				if (attempt < MAX_RETRY_COUNT) {
					try {
						Thread.sleep(RETRY_DELAY_MS);
					} catch (InterruptedException interruptedException) {
						log.error("인터럽트 예외: {}", interruptedException.getMessage());
						return null;
					}
				} else {
					log.error("최대 {}번 시도 후에도 ISBN {}에 대한 API 응답을 받지 못했습니다.", MAX_RETRY_COUNT, apiUrl);
				}
			} catch (Exception e) {
				log.error("오류: {}", e.getMessage());
				return null;
			}
		}
		return null;
	}

	/**
	 * 주어진 URL 에서 이미지를 다운로드하고, 클라우드에 업로드합니다.
	 *
	 * @param book      이미지를 업로드할 책 엔티티
	 * @param coverUrl  이미지의 URL
	 * @return 업로드된 이미지의 URL, 업로드 실패 시 {@code null}
	 */
	public String downloadAndUploadImage(Book book, String coverUrl) {
		try {
			URI uri = new URI(coverUrl);
			BufferedImage image = ImageIO.read(uri.toURL());
			String fileName = book.getBookTitle() + ".jpg";

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(image, "jpg", baos);
			byte[] imageData = baos.toByteArray();

			MultipartFile multipartFile = new CustomMultipartFile(imageData, fileName, "image/jpeg");

			log.info("책 {}에 대한 MultipartFile 준비 완료", fileName);

			return uploadService.upload(multipartFile, "books");
		} catch (IOException e) {
			log.error("책 {}에 대한 이미지를 다운로드하거나 저장하는 데 실패했습니다.", book.getBookTitle());
			return null;
		} catch (Exception e) {
			log.error("오류: {}", e.getMessage());
			return null;
		}
	}
}
