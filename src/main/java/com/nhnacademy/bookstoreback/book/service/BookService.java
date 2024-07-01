package com.nhnacademy.bookstoreback.book.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookstoreback.author.domain.entity.Author;
import com.nhnacademy.bookstoreback.author.repository.AuthorRepository;
import com.nhnacademy.bookstoreback.author.service.AuthorService;
import com.nhnacademy.bookstoreback.book.domain.dto.request.BookUpdateRequest;
import com.nhnacademy.bookstoreback.book.domain.dto.request.CreateBookRequest;
import com.nhnacademy.bookstoreback.book.domain.dto.response.BookListResponse;
import com.nhnacademy.bookstoreback.book.domain.dto.response.CreateBookResponse;
import com.nhnacademy.bookstoreback.book.domain.dto.response.GetBookDetailResponse;
import com.nhnacademy.bookstoreback.book.domain.entity.Book;
import com.nhnacademy.bookstoreback.book.repository.BookRepository;
import com.nhnacademy.bookstoreback.bookstatus.domain.entity.BookStatus;
import com.nhnacademy.bookstoreback.bookstatus.repository.BookStatusRepository;
import com.nhnacademy.bookstoreback.bookstatus.service.BookStatusService;
import com.nhnacademy.bookstoreback.category.domain.entity.BookCategory;
import com.nhnacademy.bookstoreback.category.repository.BookCategoryRepository;
import com.nhnacademy.bookstoreback.category.repository.CategoryRepository;
import com.nhnacademy.bookstoreback.category.service.CategoryService;
import com.nhnacademy.bookstoreback.global.exception.AlreadyExistsException;
import com.nhnacademy.bookstoreback.global.exception.NotFoundException;
import com.nhnacademy.bookstoreback.global.exception.payload.ErrorStatus;
import com.nhnacademy.bookstoreback.publisher.domain.entity.Publisher;
import com.nhnacademy.bookstoreback.publisher.repository.PublisherRepository;
import com.nhnacademy.bookstoreback.publisher.service.PublisherService;
import com.nhnacademy.bookstoreback.tag.domain.entity.BookTag;
import com.nhnacademy.bookstoreback.tag.repository.BookTagRepository;
import com.nhnacademy.bookstoreback.tag.repository.TagRepository;

import lombok.RequiredArgsConstructor;

/**
 * 도서 Service
 *
 * @author 김기욱
 * @version 1.0
 */
@Service
@Transactional
@RequiredArgsConstructor
public class BookService {
	private final RestTemplate restTemplate;
	private final BookRepository bookRepository;
	private final AuthorRepository authorRepository;
	private final PublisherRepository publisherRepository;
	private final BookStatusRepository bookStatusRepository;
	private final CategoryRepository categoryRepository;
	private final BookCategoryRepository bookCategoryRepository;
	private final AuthorService authorService;
	private final PublisherService publisherService;
	private final BookStatusService bookStatusService;
	private final CategoryService categoryService;
	private final TagRepository tagRepository;
	private final BookTagRepository bookTagRepository;

	/**
	 * 도서 ID를 기준으로 도서 조회
	 *
	 * @param bookId 도서 ID
	 * @return 도서 상세 정보
	 */
	public GetBookDetailResponse getBook(Long bookId) {
		Book book = bookRepository.findById(bookId).orElse(null);
		if (book != null) {
			return GetBookDetailResponse.fromEntity(book);
		}
		return null;
	}

	/**
	 * 도서 ID를 기준으로 도서 패키징 여부 업데이트
	 *
	 * @param bookId 도서 ID
	 */
	public void updateBookById(Long bookId) {
		Book book = bookRepository.findById(bookId).orElse(null);
		if (book != null) {
			book.update(true);
			bookRepository.save(book);
		}
	}

	/**
	 * 도서 리스트 조회 및 저장 (베스트셀러, 신간, 주목할만한 신간 등)
	 *
	 * @param apiUrl 도서 정보 API URL
	 */
	public void fetchAndSaveBooks(String apiUrl) {
		String response = restTemplate.getForObject(apiUrl, String.class);

		ObjectMapper objectMapper = new ObjectMapper();
		try {
			JsonNode root = objectMapper.readTree(response);
			JsonNode items = root.path("item");

			for (JsonNode item : items) {
				saveBook(item);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * ISBN을 기준으로 도서 조회 및 저장
	 *
	 * @param apiURL 도서 정보 API URL
	 */
	public void saveBookByIsbn(String apiURL) {
		try {
			// API URL을 이용하여 도서 정보를 조회
			String response = restTemplate.getForObject(apiURL, String.class);

			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode root = objectMapper.readTree(response);
			JsonNode item = root.path("item").get(0);

			saveBook(item);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 도서 저장 메소드
	 *
	 * @param item 도서 정보
	 */
	private void saveBook(JsonNode item) throws Exception {
		// Book 정보 파싱
		String bookIsbn = item.path("isbn13").asText("");

		// ISBN을 기준으로 책이 이미 존재하는지 확인
		if (bookRepository.findByBookIsbn(bookIsbn).isPresent()) {
			return; // 이미 존재하는 책이면 저장하지 않음
		}

		String bookTitle = item.path("title").asText();
		String bookIndex = "";
		String bookDescription = item.path("description").asText("");
		boolean bookPackaging = false;  // Default value as there is no packaging info in JSON
		String dateString = item.path("pubDate").asText();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date bookPublishDate = sdf.parse(dateString);
		BigDecimal bookPrice = BigDecimal.valueOf(item.path("priceStandard").asDouble());
		BigDecimal bookSalePrice = BigDecimal.valueOf(item.path("priceSales").asDouble());
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
		String stockStatus = item.path("stockStatus").asText("");
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
	}

	/**
	 * ISBN을 기준으로 도서 업데이트
	 *
	 * @param isbn ISBN
	 */
	public GetBookDetailResponse updateBook(String isbn, BookUpdateRequest request) {
		// 1. ISBN으로 책을 찾습니다.
		Optional<Book> optionalBook = bookRepository.findByBookIsbn(isbn);
		if (optionalBook.isEmpty()) {
			throw new NoSuchElementException("No book found with ISBN: " + isbn);
		}

		Book book = optionalBook.get();

		// 2. 책의 정보를 업데이트합니다.
		BookStatus bookStatus = bookStatusService.findOrCreateBookStatus(request.statusName());
		book.setBookStatus(bookStatus);
		book.setBookDescription(request.description());
		book.setBookIndex(request.index());
		book.setBookQuantity(request.quantity());
		book.setBookPrice(request.price());
		book.setBookSalePrice(request.salePrice());

		// 새로운 할인율을 계산합니다.
		BigDecimal bookSalePercent = request.price().subtract(request.salePrice())
			.divide(request.price(), 4, RoundingMode.HALF_UP)
			.multiply(BigDecimal.valueOf(100));
		book.setBookSalePercent(bookSalePercent);

		// 3. 변경된 책을 저장합니다.
		bookRepository.save(book);

		// 4. 업데이트된 책의 정보를 BookDetailResponse DTO로 변환하여 반환합니다.
		return GetBookDetailResponse.fromEntity(book);
	}

	/**
	 * 모든 도서 조회
	 *
	 * @return 도서 리스트
	 */
	@Transactional(readOnly = true)
	public List<BookListResponse> findAllBooks() {
		// 1. 모든 책을 조회합니다.
		List<Book> books = bookRepository.findAll();

		// 2. 조회된 책들을 BookListResponse DTO로 변환합니다.
		List<BookListResponse> bookListResponses = new ArrayList<>();
		for (Book book : books) {
			BookListResponse bookListResponse = BookListResponse.builder()
				.bookId(book.getBookId())
				.bookTitle(book.getBookTitle())
				.bookIsbn(book.getBookIsbn())
				.bookPrice(book.getBookPrice())
				.bookSalePrice(book.getBookSalePrice())
				.bookSalePercent(book.getBookSalePercent())
				.build();
			bookListResponses.add(bookListResponse);
		}

		// 3. 변환된 DTO 리스트를 반환합니다.
		return bookListResponses;
	}

	/**
	 * ISBN을 기준으로 도서 조회
	 *
	 * @param isbn ISBN
	 * @return 도서 상세 정보
	 */
	@Transactional(readOnly = true)
	public GetBookDetailResponse findBookByIsbn(String isbn) {
		// ISBN을 기준으로 책을 조회합니다.
		Optional<Book> optionalBook = bookRepository.findByBookIsbn(isbn);

		if (optionalBook.isEmpty()) {
			throw new NoSuchElementException("No book found with ISBN: " + isbn);
		}

		Book book = optionalBook.get();

		// 조회된 책을 BookDetailResponse DTO로 변환합니다.

		// 변환된 DTO를 반환합니다.
		return GetBookDetailResponse.fromEntity(book);
	}

	public CreateBookResponse createBook(CreateBookRequest request) {
		if (bookRepository.existsByBookTitle(request.bookTitle())) {
			String errorMessage = String.format("해당 도서 '%s'는 이미 존재 하는 도서 입니다.", request.bookTitle());
			ErrorStatus errorStatus = ErrorStatus.from(errorMessage, HttpStatus.NOT_FOUND, LocalDateTime.now());
			throw new AlreadyExistsException(errorStatus);
		}

		Author author = authorRepository.findByAuthorName(request.authorName()).orElseThrow(() -> {
			String errorMessage = String.format("해당 작가 '%s'는 존재하지 않는 작가 입니다.", request.authorName());
			ErrorStatus errorStatus = ErrorStatus.from(errorMessage, HttpStatus.NOT_FOUND, LocalDateTime.now());
			return new NotFoundException(errorStatus);
		});

		Publisher publisher = publisherRepository.findByPublisherName(request.publisherName()).orElseThrow(() -> {
			String errorMessage = String.format("해당 출판사 '%s'는 존재하지 않는 출판사 입니다.", request.publisherName());
			ErrorStatus errorStatus = ErrorStatus.from(errorMessage, HttpStatus.NOT_FOUND, LocalDateTime.now());
			return new NotFoundException(errorStatus);
		});

		BookStatus bookStatus = bookStatusRepository.findByBookStatusName(request.bookStatusName()).orElseThrow(() -> {
			String errorMessage = String.format("해당 도서상태 '%s'는 존재하지 않는 도서상태 입니다.", request.bookStatusName());
			ErrorStatus errorStatus = ErrorStatus.from(errorMessage, HttpStatus.NOT_FOUND, LocalDateTime.now());
			return new NotFoundException(errorStatus);
		});

		Book book = bookRepository.save(Book.toEntity(request, author, publisher, bookStatus));

		List<Long> categories = request.categories();
		List<Long> tags = request.tags();

		if (categories != null) {
			categories.forEach(categoryId -> {
				categoryRepository.findById(categoryId).orElseThrow(() -> {
					String errorMessage = String.format("해당 카테고리 '%d'는 존재하지 않는 카테고리 입니다.", categoryId);
					ErrorStatus errorStatus = ErrorStatus.from(errorMessage, HttpStatus.NOT_FOUND, LocalDateTime.now());
					return new NotFoundException(errorStatus);
				});
				bookCategoryRepository.save(
					new BookCategory(book, categoryRepository.findById(categoryId).orElse(null)));
			});
		}

		if (tags != null) {
			tags.forEach(tagId -> {
				tagRepository.findById(tagId).orElseThrow(() -> {
					String errorMessage = String.format("해당 태그 '%d'는 존재하지 않는 태그 입니다.", tagId);
					ErrorStatus errorStatus = ErrorStatus.from(errorMessage, HttpStatus.NOT_FOUND, LocalDateTime.now());
					return new NotFoundException(errorStatus);
				});
				bookTagRepository.save(
					new BookTag(book, tagRepository.findById(tagId).orElse(null)));
			});
		}

		return CreateBookResponse.fromEntity(
			bookRepository.save(book));
	}

	public void deleteBook(Long bookId) {
		bookRepository.findById(bookId).orElseThrow(() -> {
			String errorMessage = String.format("해당 도서 '%d'는 존재하지 않는 도서 입니다.", bookId);
			ErrorStatus errorStatus = ErrorStatus.from(errorMessage, HttpStatus.NOT_FOUND, LocalDateTime.now());
			return new NotFoundException(errorStatus);
		});

		bookCategoryRepository.deleteByBookBookId(bookId);
		bookTagRepository.deleteAllByBookBookId(bookId);
		bookRepository.deleteById(bookId);
	}
}