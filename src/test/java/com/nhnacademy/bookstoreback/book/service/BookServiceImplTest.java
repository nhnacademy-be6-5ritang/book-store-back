package com.nhnacademy.bookstoreback.book.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookstoreback.author.domain.entity.Author;
import com.nhnacademy.bookstoreback.author.repository.AuthorRepository;
import com.nhnacademy.bookstoreback.author.service.AuthorService;
import com.nhnacademy.bookstoreback.book.domain.dto.response.BookSearchResult;
import com.nhnacademy.bookstoreback.book.domain.dto.response.GetBookDetailResponse;
import com.nhnacademy.bookstoreback.book.domain.entity.Book;
import com.nhnacademy.bookstoreback.book.repository.BookRepository;
import com.nhnacademy.bookstoreback.book.service.impl.BookServiceImpl;
import com.nhnacademy.bookstoreback.bookstatus.domain.entity.BookStatus;
import com.nhnacademy.bookstoreback.bookstatus.repository.BookStatusRepository;
import com.nhnacademy.bookstoreback.bookstatus.service.BookStatusService;
import com.nhnacademy.bookstoreback.category.domain.entity.Category;
import com.nhnacademy.bookstoreback.category.repository.BookCategoryRepository;
import com.nhnacademy.bookstoreback.category.repository.CategoryRepository;
import com.nhnacademy.bookstoreback.category.service.CategoryService;
import com.nhnacademy.bookstoreback.image.repository.BookImageRepository;
import com.nhnacademy.bookstoreback.image.service.BookImageService;
import com.nhnacademy.bookstoreback.image.service.CloudImageService;
import com.nhnacademy.bookstoreback.publisher.domain.entity.Publisher;
import com.nhnacademy.bookstoreback.publisher.repository.PublisherRepository;
import com.nhnacademy.bookstoreback.publisher.service.PublisherService;
import com.nhnacademy.bookstoreback.tag.repository.BookTagRepository;
import com.nhnacademy.bookstoreback.tag.repository.TagRepository;
import com.nhnacademy.bookstoreback.wishlist.repository.WishListRepository;

class BookServiceImplTest {

	@InjectMocks
	private BookServiceImpl bookService;

	@Mock
	private RestTemplate restTemplate;
	@Mock
	private BookRepository bookRepository;
	@Mock
	private AuthorRepository authorRepository;
	@Mock
	private PublisherRepository publisherRepository;
	@Mock
	private BookStatusRepository bookStatusRepository;
	@Mock
	private CategoryRepository categoryRepository;
	@Mock
	private BookCategoryRepository bookCategoryRepository;
	@Mock
	private BookImageRepository bookImageRepository;
	@Mock
	private AuthorService authorService;
	@Mock
	private PublisherService publisherService;
	@Mock
	private BookStatusService bookStatusService;
	@Mock
	private TagRepository tagRepository;
	@Mock
	private BookTagRepository bookTagRepository;
	@Mock
	private CategoryService categoryService;
	@Mock
	private BookImageService bookImageService;
	@Mock
	private CloudImageService cloudImageService;
	@Mock
	private WishListRepository wishListRepository;

	private ObjectMapper objectMapper;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		objectMapper = new ObjectMapper();
	}

	@Test
	void testFetchAndSaveBooks() throws Exception {
		String apiUrl = "http://example.com/api/books";
		String jsonResponse = "{ \"item\": [ { \"isbn13\": \"1234567890\", \"title\": \"Test Book\", \"description\": \"A book\", \"pubDate\": \"2024-07-29\", \"priceStandard\": 100.00, \"priceSales\": 80.00, \"author\": \"John Doe\", \"publisher\": \"Test Publisher\", \"stockStatus\": \"판매중\", \"categoryName\": \"국내도서 > Fiction\" } ] }";

		JsonNode root = objectMapper.readTree(jsonResponse);
		when(restTemplate.getForObject(apiUrl, String.class)).thenReturn(jsonResponse);

		// Mock dependencies
		when(authorService.findOrCreateAuthor("John Doe")).thenReturn(mock(Author.class));
		when(publisherService.findOrCreatePublisher("Test Publisher")).thenReturn(mock(Publisher.class));
		when(bookStatusService.findOrCreateBookStatus("판매중")).thenReturn(mock(BookStatus.class));
		when(categoryService.findOrCreateCategory("Fiction", 56L)).thenReturn(mock(Category.class));
		when(cloudImageService.uploadImageForBookByNaverApi(any(Book.class))).thenReturn(
			"http://example.com/image.jpg");
		doNothing().when(bookImageService).mapImageToBook(any(Book.class), anyString());

		bookService.fetchAndSaveBooks(apiUrl);

		verify(bookRepository, times(1)).save(any(Book.class));
	}

	@Test
	void testSaveBookByIsbn() throws Exception {
		String apiUrl = "http://example.com/api/book?isbn=1234567890";
		String jsonResponse = "{ \"item\": [ { \"isbn13\": \"1234567890\", \"title\": \"Test Book\", \"description\": \"A book\", \"pubDate\": \"2024-07-29\", \"priceStandard\": 100.00, \"priceSales\": 80.00, \"author\": \"John Doe\", \"publisher\": \"Test Publisher\", \"stockStatus\": \"판매중\", \"categoryName\": \"국내도서 > Fiction\" } ] }";

		JsonNode root = objectMapper.readTree(jsonResponse);
		when(restTemplate.getForObject(apiUrl, String.class)).thenReturn(jsonResponse);

		// Mock dependencies
		when(authorService.findOrCreateAuthor("John Doe")).thenReturn(mock(Author.class));
		when(publisherService.findOrCreatePublisher("Test Publisher")).thenReturn(mock(Publisher.class));
		when(bookStatusService.findOrCreateBookStatus("판매중")).thenReturn(mock(BookStatus.class));
		when(categoryService.findOrCreateCategory("Fiction", 56L)).thenReturn(mock(Category.class));
		when(cloudImageService.uploadImageForBookByNaverApi(any(Book.class))).thenReturn(
			"http://example.com/image.jpg");
		doNothing().when(bookImageService).mapImageToBook(any(Book.class), anyString());

		bookService.saveBookByIsbn(apiUrl);

		verify(bookRepository, times(1)).save(any(Book.class));
	}

	@Test
	void testSaveBook() throws Exception {
		// Prepare a mock JSON node
		JsonNode item = objectMapper.readTree(
			"{ \"isbn13\": \"1234567890\", \"title\": \"Test Book\", \"description\": \"A book\", \"pubDate\": \"2024-07-29\", \"priceStandard\": 100.00, \"priceSales\": 80.00, \"author\": \"John Doe\", \"publisher\": \"Test Publisher\", \"stockStatus\": \"판매중\", \"categoryName\": \"국내도서 > Fiction\" }");

		// Mock dependencies
		when(authorService.findOrCreateAuthor("John Doe")).thenReturn(mock(Author.class));
		when(publisherService.findOrCreatePublisher("Test Publisher")).thenReturn(mock(Publisher.class));
		when(bookStatusService.findOrCreateBookStatus("판매중")).thenReturn(mock(BookStatus.class));
		when(categoryService.findOrCreateCategory("Fiction", 56L)).thenReturn(mock(Category.class));
		when(cloudImageService.uploadImageForBookByNaverApi(any(Book.class))).thenReturn(
			"http://example.com/image.jpg");
		doNothing().when(bookImageService).mapImageToBook(any(Book.class), anyString());

		bookService.saveBook(item);

		verify(bookRepository, times(1)).save(any(Book.class));
	}

	@Test
	void testGetNewestBooks() {
		// Given
		Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "bookPublishDate"));

		// Mocking Author object
		Author mockAuthor = mock(Author.class);
		when(mockAuthor.getAuthorName()).thenReturn("John Doe");

		// Mocking Publisher object
		Publisher mockPublisher = mock(Publisher.class);
		when(mockPublisher.getPublisherName()).thenReturn("Test Publisher");

		// Mocking BookStatus object
		BookStatus mockBookStatus = mock(BookStatus.class);
		when(mockBookStatus.getBookStatusName()).thenReturn("판매중");

		// Mocking Book objects
		Book book1 = mock(Book.class);
		Book book2 = mock(Book.class);

		// Ensure that getBookImages() returns an empty list
		when(book1.getBookImages()).thenReturn(Collections.emptyList());
		when(book2.getBookImages()).thenReturn(Collections.emptyList());

		// Ensure that getAuthor() returns a mock Author
		when(book1.getAuthor()).thenReturn(mockAuthor);
		when(book2.getAuthor()).thenReturn(mockAuthor);

		// Ensure that getPublisher() returns a mock Publisher
		when(book1.getPublisher()).thenReturn(mockPublisher);
		when(book2.getPublisher()).thenReturn(mockPublisher);

		// Ensure that getBookStatus() returns a mock BookStatus
		when(book1.getBookStatus()).thenReturn(mockBookStatus);
		when(book2.getBookStatus()).thenReturn(mockBookStatus);

		List<Book> books = Arrays.asList(book1, book2);
		Page<Book> bookPage = new PageImpl<>(books, pageable, books.size());

		when(bookRepository.findAllByOrderByBookPublishDateDesc(pageable)).thenReturn(bookPage);

		// When
		List<GetBookDetailResponse> result = bookService.getNewestBooks();

		// Then
		assertNotNull(result);
		assertEquals(books.size(), result.size());
		verify(bookRepository, times(1)).findAllByOrderByBookPublishDateDesc(pageable);
	}

	@Test
	void testGetBook() {

		// Mocking Author object
		Author mockAuthor = mock(Author.class);
		when(mockAuthor.getAuthorName()).thenReturn("John Doe");

		// Mocking Publisher object
		Publisher mockPublisher = mock(Publisher.class);
		when(mockPublisher.getPublisherName()).thenReturn("Test Publisher");

		// Mocking BookStatus object
		BookStatus mockBookStatus = mock(BookStatus.class);
		when(mockBookStatus.getBookStatusName()).thenReturn("판매중");

		// Mocking Book objects
		Book book1 = mock(Book.class);
		Book book2 = mock(Book.class);

		// Ensure that getBookImages() returns an empty list
		when(book1.getBookImages()).thenReturn(Collections.emptyList());
		when(book2.getBookImages()).thenReturn(Collections.emptyList());

		// Ensure that getAuthor() returns a mock Author
		when(book1.getAuthor()).thenReturn(mockAuthor);
		when(book2.getAuthor()).thenReturn(mockAuthor);

		// Ensure that getPublisher() returns a mock Publisher
		when(book1.getPublisher()).thenReturn(mockPublisher);
		when(book2.getPublisher()).thenReturn(mockPublisher);

		// Ensure that getBookStatus() returns a mock BookStatus
		when(book1.getBookStatus()).thenReturn(mockBookStatus);
		when(book2.getBookStatus()).thenReturn(mockBookStatus);
		// Given
		Long bookId = 1L;
		when(bookRepository.findById(bookId)).thenReturn(Optional.of(book1));

		// When
		GetBookDetailResponse result = bookService.getBook(bookId);

		// Then
		assertNotNull(result);
		verify(bookRepository, times(1)).findById(bookId);
	}

	@Test
	void testFindBookByIsbn() {
		// Mocking Author object
		Author mockAuthor = mock(Author.class);
		when(mockAuthor.getAuthorName()).thenReturn("John Doe");

		// Mocking Publisher object
		Publisher mockPublisher = mock(Publisher.class);
		when(mockPublisher.getPublisherName()).thenReturn("Test Publisher");

		// Mocking BookStatus object
		BookStatus mockBookStatus = mock(BookStatus.class);
		when(mockBookStatus.getBookStatusName()).thenReturn("판매중");

		// Mocking Book objects
		Book book1 = mock(Book.class);
		Book book2 = mock(Book.class);

		// Ensure that getBookImages() returns an empty list
		when(book1.getBookImages()).thenReturn(Collections.emptyList());
		when(book2.getBookImages()).thenReturn(Collections.emptyList());

		// Ensure that getAuthor() returns a mock Author
		when(book1.getAuthor()).thenReturn(mockAuthor);
		when(book2.getAuthor()).thenReturn(mockAuthor);

		// Ensure that getPublisher() returns a mock Publisher
		when(book1.getPublisher()).thenReturn(mockPublisher);
		when(book2.getPublisher()).thenReturn(mockPublisher);

		// Ensure that getBookStatus() returns a mock BookStatus
		when(book1.getBookStatus()).thenReturn(mockBookStatus);
		when(book2.getBookStatus()).thenReturn(mockBookStatus);

		// Given
		String isbn = "1234567890";
		when(bookRepository.findByBookIsbn(isbn)).thenReturn(Optional.of(book1));

		// When
		GetBookDetailResponse result = bookService.findBookByIsbn(isbn);

		// Then
		assertNotNull(result);
		verify(bookRepository, times(1)).findByBookIsbn(isbn);
	}

	@Test
	void testDeleteBook() {
		// Given
		Long bookId = 1L;
		when(bookRepository.existsById(bookId)).thenReturn(true);

		// When
		bookService.deleteBook(bookId);

		// Then
		verify(bookCategoryRepository, times(1)).deleteAllByBookBookId(bookId);
		verify(bookTagRepository, times(1)).deleteAllByBookBookId(bookId);
		verify(bookImageRepository, times(1)).deleteAllByBookBookId(bookId);
		verify(bookRepository, times(1)).deleteById(bookId);
	}

	@Test
	void testUpdateQuantity() {
		// Given
		Long bookId = 1L;
		int quantity = 10;
		Book book = new Book(/* 초기화 */);
		when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

		// When
		bookService.updateQuantity(bookId, quantity);

		// Then
		verify(bookRepository, times(1)).save(any(Book.class));
	}

	@Test
	void testSearchBooks() {
		// Given
		String title = "Test Book";
		List<BookSearchResult> results = Arrays.asList(mock(BookSearchResult.class));
		when(bookRepository.findByBookTitleContainingIgnoreCaseCustom(title)).thenReturn(results);

		// When
		List<BookSearchResult> result = bookService.searchBooks(title);

		// Then
		assertNotNull(result);
		assertEquals(results.size(), result.size());
		verify(bookRepository, times(1)).findByBookTitleContainingIgnoreCaseCustom(title);
	}

}