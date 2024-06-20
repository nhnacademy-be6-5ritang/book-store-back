package com.nhnacademy.bookstoreback.book.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookstoreback.book.domain.entity.Author;
import com.nhnacademy.bookstoreback.book.domain.entity.Book;
import com.nhnacademy.bookstoreback.book.domain.entity.BookStatus;
import com.nhnacademy.bookstoreback.book.domain.entity.Publisher;
import com.nhnacademy.bookstoreback.book.repository.BookRepository;

@Service
public class BookService {

	private final RestTemplate restTemplate;
	private final BookRepository bookRepository;
	private final AuthorService authorService;
	private final PublisherService publisherService;
	private final BookStatusService bookStatusService;

	@Autowired
	public BookService(RestTemplate restTemplate, BookRepository bookRepository, AuthorService authorService,
		PublisherService publisherService, BookStatusService bookStatusService) {
		this.restTemplate = restTemplate;
		this.bookRepository = bookRepository;
		this.authorService = authorService;
		this.publisherService = publisherService;
		this.bookStatusService = bookStatusService;
	}

	public void fetchAndSaveBook(String apiUrl) {
		String response = restTemplate.getForObject(apiUrl, String.class);

		ObjectMapper objectMapper = new ObjectMapper();
		try {
			JsonNode root = objectMapper.readTree(response);
			JsonNode item = root.path("item").get(0);

			// Book 정보 파싱
			String bookTitle = item.path("title").asText();
			String bookIndex = "";
			String bookDescription = item.path("description").asText();
			boolean bookPackaging = false;  // Default value as there is no packaging info in JSON
			String dateString = item.path("pubDate").asText();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date bookPublishDate = sdf.parse(dateString);
			String bookIsbn = item.path("isbn13").asText();
			BigDecimal bookPrice = new BigDecimal(item.path("priceStandard").asDouble());
			BigDecimal bookSalePrice = new BigDecimal(item.path("priceSales").asDouble());
			BigDecimal bookSalePercent = bookPrice.subtract(bookSalePrice)
				.divide(bookPrice, 4, RoundingMode.HALF_UP)
				.multiply(BigDecimal.valueOf(100));

			// Author 정보 파싱 및 저장
			String authorName = item.path("author").asText();
			Author author = authorService.findOrCreateAuthor(authorName);

			// Publisher 정보 파싱 및 저장
			String publisherName = item.path("publisher").asText();
			Publisher publisher = publisherService.findOrCreatePublisher(publisherName);

			// BookStatus 정보 파싱 및 저장
			String stockStatus = item.path("stockStatus").asText();
			String statusName;
			if (stockStatus.isEmpty()) {
				statusName = "ON_SALE";
			} else if (stockStatus.contains("품절")) {
				statusName = "SOLD_OUT";
			} else if (stockStatus.contains("절판")) {
				statusName = "DELETED";
			} else {
				statusName = "UNKNOWN";  // 추가적으로 필요에 따라 상태를 정의할 수 있습니다.
			}
			BookStatus bookStatus = bookStatusService.findOrCreateBookStatus(statusName);

			// 새로운 Book 엔티티 생성 및 저장
			Book book = new Book();
			book.setBookTitle(bookTitle);
			book.setBookIndex(bookIndex);
			book.setBookDescription(bookDescription);
			book.setBookPackaging(bookPackaging);
			book.setBookPublishDate(bookPublishDate);
			book.setBookIsbn(bookIsbn);
			book.setBookPrice(bookPrice);
			book.setBookSalePercent(bookSalePercent);
			book.setBookSalePrice(bookSalePrice);
			book.setAuthor(author);
			book.setPublisher(publisher);
			book.setBookStatus(bookStatus);

			bookRepository.save(book);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
