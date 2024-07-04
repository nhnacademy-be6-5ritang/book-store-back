package com.nhnacademy.bookstoreback.image.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookstoreback.book.domain.entity.Book;
import com.nhnacademy.bookstoreback.book.repository.BookRepository;
import com.nhnacademy.bookstoreback.image.domain.entity.Image;
import com.nhnacademy.bookstoreback.image.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CloudImageService {

	@Value("${nhncloud.appkey}")
	private String appKey;

	@Value("${nhncloud.secretkey}")
	private String secretKey;

	@Value("${naver.client.id}")
	private String naverClientId;

	@Value("${naver.client.secret}")
	private String naverClientSecret;

	private final RestTemplate restTemplate;
	private final ObjectMapper objectMapper;

	private static final String LOCAL_DIRECTORY = "cover_images"; // 로컬 저장 디렉토리
	private static final String CLOUD_PATH = "/5ritang/books"; // NHN 클라우드 저장 경로
	private static final int MAX_RETRY_COUNT = 3; // 최대 재시도 횟수
	private static final int RETRY_DELAY_MS = 2000; // 재시도 간 대기 시간 (밀리초)

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private ImageRepository imageRepository;

	@Autowired
	public CloudImageService(RestTemplate restTemplate, ObjectMapper objectMapper) {
		this.restTemplate = restTemplate;
		this.objectMapper = objectMapper;
	}

	@Transactional
	public void downloadCoverImages() {
		List<Book> books = bookRepository.findAll();

		// 디렉토리 생성
		File directory = new File(LOCAL_DIRECTORY);
		if (!directory.exists()) {
			directory.mkdirs();
		}

		for (Book book : books) {
			String isbn = book.getBookIsbn();
			String apiUrl = "https://openapi.naver.com/v1/search/book.json?query=" + isbn;

			HttpHeaders headers = new HttpHeaders();
			headers.set("X-Naver-Client-Id", naverClientId);
			headers.set("X-Naver-Client-Secret", naverClientSecret);

			HttpEntity<String> requestEntity = new HttpEntity<>(headers);

			int attempt = 0;
			boolean success = false;

			while (!success && attempt < MAX_RETRY_COUNT) {
				try {
					ResponseEntity<Map> response = restTemplate.exchange(apiUrl, HttpMethod.GET, requestEntity, Map.class);

					Map<String, Object> body = response.getBody();
					if (body == null) {
						System.out.println("Failed to get API response for ISBN: " + isbn);
						break;
					}

					List<Object> items = (List<Object>) body.get("items");
					if (items == null || items.isEmpty()) {
						System.out.println("No items found in API response for ISBN: " + isbn);
						break;
					}

					Map<String, Object> item = (Map<String, Object>) items.get(0);
					String coverUrl = (String) item.get("image");

					try {
						URL url = new URL(coverUrl);
						BufferedImage image = ImageIO.read(url);
						String fileName = book.getBookTitle() + ".jpg";
						File outputfile = new File(LOCAL_DIRECTORY + File.separator + fileName);
						ImageIO.write(image, "jpg", outputfile);
						System.out.println("Saved image for book: " + book.getBookTitle());

						// 이미지 업로드 후 Image 엔티티 저장
						String imageUrl = uploadImage(outputfile.getAbsolutePath(), fileName);
						if (imageUrl != null) {
							Image savedImage = new Image(book.getBookTitle(), imageUrl);
							if (imageRepository.findByImageName(book.getBookTitle()).isEmpty()) {
								imageRepository.save(savedImage);
								System.out.println("Saved image information to database: " + savedImage);
							} else {
								System.out.println("Image with name " + fileName + " already exists in the database. Skipping save.");
							}
						}

						success = true;

					} catch (IOException e) {
						e.printStackTrace();
						System.out.println("Failed to download or save image for book: " + book.getBookTitle());
					}

				} catch (HttpClientErrorException.TooManyRequests e) {
					attempt++;
					if (attempt < MAX_RETRY_COUNT) {
						try {
							Thread.sleep(RETRY_DELAY_MS);
						} catch (InterruptedException interruptedException) {
							interruptedException.printStackTrace();
							break;
						}
					} else {
						System.out.println("Failed to get API response for ISBN: " + isbn + " after " + MAX_RETRY_COUNT + " attempts.");
					}
				} catch (Exception e) {
					e.printStackTrace();
					break;
				}
			}

			if (!success) {
				System.out.println("Giving up on ISBN: " + isbn + " after " + MAX_RETRY_COUNT + " attempts.");
			}
		}
	}

	public String uploadImage(String localFilePath, String imageName) {
		try {
			// 이미지 제목으로 이미지 조회
			Optional<Image> existingImage = imageRepository.findByImageName(imageName);
			if (existingImage.isPresent()) {
				System.out.println("Image with name " + imageName + " already exists in the database. Skipping upload.");
				return existingImage.get().getImageUrl();
			}

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.MULTIPART_FORM_DATA);
			headers.set("Authorization", secretKey);

			MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
			body.add("files", new FileSystemResource(new File(localFilePath)));

			Map<String, Object> params = new HashMap<>();
			params.put("basepath", CLOUD_PATH);
			params.put("overwrite", true);

			body.add("params", objectMapper.writeValueAsString(params));

			HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

			String url = "https://api-image.nhncloudservice.com/image/v2.0/appkeys/" + appKey + "/images";

			ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);

			String jsonResponse = response.getBody();

			// JSON 응답을 파싱하여 URL 필드를 추출
			Map<String, Object> responseMap = objectMapper.readValue(jsonResponse, Map.class);

			// 응답 내용 로깅
			System.out.println("Upload response: " + responseMap);

			// "successes" 배열에서 첫 번째 객체의 "url" 필드 추출
			if (responseMap != null && responseMap.containsKey("successes")) {
				List<Map<String, Object>> successes = (List<Map<String, Object>>) responseMap.get("successes");
				if (!successes.isEmpty()) {
					Map<String, Object> firstSuccess = successes.get(0);
					return (String) firstSuccess.get("url");
				} else {
					throw new RuntimeException("No successes found in the response");
				}
			} else {
				throw new RuntimeException("Successes array not found in the response");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Failed to upload image to NHN Cloud", e);
		}
	}

	public void uploadAllImages() {
		String localDirectoryPath = LOCAL_DIRECTORY;

		// 상대 경로를 절대 경로로 변환
		String absolutePath = new File(localDirectoryPath).getAbsolutePath();
		File directory = new File(absolutePath);

		File[] files = directory.listFiles((dir, name) -> name.toLowerCase().endsWith(".jpg"));

		if (files != null) {
			for (File file : files) {
				uploadImage(file.getAbsolutePath(), file.getName());
			}
		}
	}
}
