package com.nhnacademy.bookstoreback.book.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookstoreback.author.domain.entity.Author;
import com.nhnacademy.bookstoreback.author.exception.AuthorNotFoundException;
import com.nhnacademy.bookstoreback.author.repository.AuthorRepository;
import com.nhnacademy.bookstoreback.author.service.AuthorService;
import com.nhnacademy.bookstoreback.book.domain.dto.request.CreateBookRequest;
import com.nhnacademy.bookstoreback.book.domain.dto.request.UpdateBookRequest;
import com.nhnacademy.bookstoreback.book.domain.dto.response.BookSearchResult;
import com.nhnacademy.bookstoreback.book.domain.dto.response.GetBookDetailResponse;
import com.nhnacademy.bookstoreback.book.domain.entity.Book;
import com.nhnacademy.bookstoreback.book.exception.BookAlreadyExistsException;
import com.nhnacademy.bookstoreback.book.exception.BookNotFoundException;
import com.nhnacademy.bookstoreback.book.repository.BookRepository;
import com.nhnacademy.bookstoreback.book.service.BookService;
import com.nhnacademy.bookstoreback.bookstatus.domain.entity.BookStatus;
import com.nhnacademy.bookstoreback.bookstatus.exception.BookStatusNotFoundException;
import com.nhnacademy.bookstoreback.bookstatus.repository.BookStatusRepository;
import com.nhnacademy.bookstoreback.bookstatus.service.BookStatusService;
import com.nhnacademy.bookstoreback.category.domain.entity.BookCategory;
import com.nhnacademy.bookstoreback.category.domain.entity.Category;
import com.nhnacademy.bookstoreback.category.exception.CategoryNotFoundException;
import com.nhnacademy.bookstoreback.category.repository.BookCategoryRepository;
import com.nhnacademy.bookstoreback.category.repository.CategoryRepository;
import com.nhnacademy.bookstoreback.category.service.BookCategoryService;
import com.nhnacademy.bookstoreback.category.service.CategoryService;
import com.nhnacademy.bookstoreback.image.controller.BookImageController;
import com.nhnacademy.bookstoreback.image.controller.CoverImageController;
import com.nhnacademy.bookstoreback.image.repository.BookImageRepository;
import com.nhnacademy.bookstoreback.publisher.domain.entity.Publisher;
import com.nhnacademy.bookstoreback.publisher.exception.PublisherNotFoundException;
import com.nhnacademy.bookstoreback.publisher.repository.PublisherRepository;
import com.nhnacademy.bookstoreback.publisher.service.PublisherService;
import com.nhnacademy.bookstoreback.tag.domain.entity.BookTag;
import com.nhnacademy.bookstoreback.tag.exception.TagNotFoundException;
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
public class BookServiceImpl implements BookService {
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
	private final TagRepository tagRepository;
	private final BookTagRepository bookTagRepository;
	private final CategoryService categoryService;
	private final BookCategoryService bookCategoryService;
	private final BookImageRepository bookImageRepository;
	private final CoverImageController coverImageController;
	private final BookImageController bookImageController;

	/**
	 * 도서 리스트 조회 및 저장 (베스트셀러, 신간, 주목할만한 신간 등)
	 *
	 * @param apiUrl 도서 정보 API URL
	 */
	@Override
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
	@Override
	public void saveBookByIsbn(String apiURL) {
		try {
			// API URL을 이용하여 도서 정보를 조회
			String response = restTemplate.getForObject(apiURL, String.class);

			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode root = objectMapper.readTree(response);
			JsonNode item = root.path("item").get(0);

			Book book = saveBook(item);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 도서 저장 메소드
	 *
	 * @param item 도서 정보
	 */
	@Override
	public Book saveBook(JsonNode item) throws Exception {
		// Book 정보 파싱
		String bookIsbn = item.path("isbn13").asText("");

		// ISBN을 기준으로 책이 이미 존재하는지 확인
		if (bookRepository.findByBookIsbn(bookIsbn).isPresent()) {
			return null; // 이미 존재하는 책이면 저장하지 않음
		}

		String bookTitle = item.path("title").asText();
		String bookDescription = item.path("description").asText("");
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
			statusName = "판매중";
		} else if (stockStatus.contains("품절")) {
			statusName = "품절";
		} else if (stockStatus.contains("절판")) {
			statusName = "절판";
		} else {
			statusName = "판매중";  // 추가적으로 필요에 따라 상태를 정의할 수 있습니다.
		}
		BookStatus bookStatus = bookStatusService.findOrCreateBookStatus(statusName);

		// Category 정보 파싱 및 저장
		String categoryNameString = item.path("categoryName").asText();
		String[] categoryParts = categoryNameString.split(">");
		String parentCategoryName = categoryParts[0].trim();
		String categoryName = categoryParts[1].trim();

		Long parentCategoryId = parentCategoryName.equals("국내도서") ? 56L : 57L;
		Category category = categoryService.findOrCreateCategory(categoryName, parentCategoryId);

		// 새로운 Book 엔티티 생성 및 저장
		Book book = new Book();
		book.setBookTitle(bookTitle);
		book.setBookDescription(bookDescription);
		book.setBookPublishDate(bookPublishDate);
		book.setBookIsbn(bookIsbn);
		book.setBookPrice(bookPrice);
		book.setBookSalePercent(bookSalePercent);
		book.setBookSalePrice(bookSalePrice);
		book.setAuthor(author);
		book.setPublisher(publisher);
		book.setBookStatus(bookStatus);

		bookRepository.save(book);

		// BookCategory 엔티티 생성 및 저장
		bookCategoryService.saveBookCategory(book, category);

		coverImageController.downloadImage(book);
		bookImageController.mapImageForSingleBook(book);

		return book;
	}

	/**
	 * 모든 도서를 조회
	 *
	 * @return 도서 리스트를 포함하는 List 객체
	 */
	@Transactional(readOnly = true)
	@Override
	public List<GetBookDetailResponse> getNewestBooks() {
		Pageable topTenNewest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "bookPublishDate"));
		Page<Book> newestBooksPage = bookRepository.findAllByOrderByBookPublishDateDesc(topTenNewest);
		List<Book> newestBooks = newestBooksPage.getContent();
		return newestBooks.stream()
			.map(GetBookDetailResponse::fromEntity)
			.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	@Override
	public List<GetBookDetailResponse> getOrderedBooks() {
		Pageable topTen = PageRequest.of(0, 10);
		Page<Book> topOrderedBooksPage = bookRepository.findTopOrderedBooks(topTen);
		List<Book> topOrderedBooks = new ArrayList<>(topOrderedBooksPage.getContent());

		if (topOrderedBooks.size() < 10) {
			int booksToAdd = 10 - topOrderedBooks.size();
			List<Book> additionalBooks = bookRepository.findRandomOrderedBooks(Pageable.ofSize(booksToAdd));
			topOrderedBooks.addAll(additionalBooks);
		}

		return topOrderedBooks.stream()
			.map(GetBookDetailResponse::fromEntity)
			.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	@Override
	public List<GetBookDetailResponse> getLikesBooks() {
		Pageable topTen = PageRequest.of(0, 10);
		Page<Book> topLikedBooksPage = bookRepository.findTopLikedBooks(topTen);
		List<Book> topLikedBooks = new ArrayList<>(topLikedBooksPage.getContent());

		if (topLikedBooks.size() < 10) {
			int booksToAdd = 10 - topLikedBooks.size();
			List<Book> additionalBooks = bookRepository.findRandomLikedBooks(Pageable.ofSize(booksToAdd));
			topLikedBooks.addAll(additionalBooks);
		}

		return topLikedBooks.stream()
			.map(GetBookDetailResponse::fromEntity)
			.collect(Collectors.toList());
	}

	/**
	 * 모든 도서를 페이지네이션하여 조회
	 *
	 * @param pageable 페이지네이션 정보를 포함하는 객체
	 * @return 페이지네이션된 도서 리스트를 포함하는 Page 객체
	 */
	@Transactional(readOnly = true)
	@Override
	public Page<GetBookDetailResponse> findAllBooks(Pageable pageable) {
		int page = Math.max(pageable.getPageNumber() - 1, 0);
		int pageSize = pageable.getPageSize();

		return bookRepository.findAll(
				PageRequest.of(page, pageSize, Sort.by(Sort.Direction.ASC, "bookId")))
			.map(GetBookDetailResponse::fromEntity);
	}

	/**
	 * 도서 ID를 기준으로 도서 조회
	 *
	 * @param bookId 도서 ID
	 * @return 도서 상세 정보
	 */
	@Override
	public GetBookDetailResponse getBook(Long bookId) {
		Book book = bookRepository.findById(bookId).orElse(null);
		if (book != null) {
			return GetBookDetailResponse.fromEntity(book);
		}
		return null;
	}

	/**
	 * ISBN을 기준으로 도서 조회
	 *
	 * @param isbn ISBN
	 * @return 도서 상세 정보
	 */
	@Transactional(readOnly = true)
	@Override
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

	@Override
	public void createBook(CreateBookRequest request) {
		if (bookRepository.existsByBookTitle(request.bookTitle())) {
			throw new BookAlreadyExistsException(request.bookTitle());
		}

		Author author = authorService.findOrCreateAuthor(request.authorName());

		Publisher publisher = publisherService.findOrCreatePublisher(request.publisherName());

		BookStatus bookStatus = bookStatusRepository.findByBookStatusName(request.bookStatusName())
			.orElseThrow(() -> new BookStatusNotFoundException(request.bookStatusName()));

		Book book = bookRepository.save(Book.toEntity(request, author, publisher, bookStatus));

		List<Long> categories = request.categories();
		List<Long> tags = request.tags();

		if (categories != null) {
			categories.forEach(categoryId -> {
				categoryRepository.findById(categoryId).orElseThrow(() -> new CategoryNotFoundException(categoryId));
				bookCategoryRepository.save(
					new BookCategory(book, categoryRepository.findById(categoryId).orElse(null)));
			});
		}

		if (tags != null) {
			tags.forEach(tagId -> {
				tagRepository.findById(tagId).orElseThrow(() -> new TagNotFoundException(tagId));
				bookTagRepository.save(
					new BookTag(book, tagRepository.findById(tagId).orElse(null)));
			});
		}
		bookRepository.save(book);
	}

	@Override
	public void updateBookById(Long bookId, UpdateBookRequest request) {
		Book book = bookRepository.findById(bookId).orElseThrow(() -> new BookNotFoundException(bookId));

		Author author = authorRepository.findByAuthorName(request.authorName())
			.orElseThrow(() -> new AuthorNotFoundException(request.authorName()));

		Publisher publisher = publisherRepository.findByPublisherName(request.publisherName())
			.orElseThrow(() -> new PublisherNotFoundException(request.publisherName()));

		BookStatus bookStatus = bookStatusRepository.findByBookStatusName(request.bookStatusName())
			.orElseThrow(() -> new BookStatusNotFoundException(request.bookStatusName()));

		// 책 내용 수정
		book.updateBook(author, publisher, bookStatus, request.bookTitle(),
			request.bookDescription(), request.bookQuantity(), request.bookPublishDate(),
			request.bookIsbn(), request.bookPrice(), request.bookSalePercent(), request.bookSalePrice());

		List<Long> categories = request.categories();
		List<Long> tags = request.tags();

		// 기존 카테고리, 태그 매핑 제거
		bookCategoryRepository.deleteAllByBookBookId(bookId);
		bookTagRepository.deleteAllByBookBookId(bookId);

		// 수정된 카테고리, 태그 매핑 추가
		if (categories != null) {
			categories.forEach(categoryId -> {
				categoryRepository.findById(categoryId).orElseThrow(() -> new CategoryNotFoundException(categoryId));
				bookCategoryRepository.save(
					new BookCategory(book, categoryRepository.findById(categoryId).orElse(null)));
			});
		}

		if (tags != null) {
			tags.forEach(tagId -> {
				tagRepository.findById(tagId).orElseThrow(() -> new TagNotFoundException(tagId));
				bookTagRepository.save(
					new BookTag(book, tagRepository.findById(tagId).orElse(null)));
			});
		}
	}

	@Override
	public void deleteBook(Long bookId) {
		if (!bookRepository.existsById(bookId)) {
			throw new BookNotFoundException(bookId);
		}

		// 해당 도서가 가지고 있는 카테고리, 태그 매핑 정보도 같이 삭제
		bookCategoryRepository.deleteAllByBookBookId(bookId);
		bookTagRepository.deleteAllByBookBookId(bookId);
		bookImageRepository.deleteAllByBookBookId(bookId);
		bookRepository.deleteById(bookId);
	}

	public void updateQuantity(Long bookId, int quantity) {
		Book book = bookRepository.findById(bookId).orElseThrow(() -> new BookNotFoundException(bookId));
		book.updateQuantitiy(quantity);
		bookRepository.save(book);
	}

	@Transactional(readOnly = true)
	@Override
	public List<BookSearchResult> searchBooks(String title) {
		return bookRepository.findByBookTitleContainingIgnoreCaseCustom(title);

	}

	@Transactional(readOnly = true)
	public Page<GetBookDetailResponse> findAllBooksByCategoryName(Pageable pageable, String categoryName) {
		int page = Math.max(pageable.getPageNumber() - 1, 0);
		int pageSize = pageable.getPageSize();

		return bookRepository.findAllByBookCategories_Category_CategoryName(
				PageRequest.of(page, pageSize, Sort.by(Sort.Direction.ASC, "bookTitle")), categoryName)
			.map(GetBookDetailResponse::fromEntity);
	}

}