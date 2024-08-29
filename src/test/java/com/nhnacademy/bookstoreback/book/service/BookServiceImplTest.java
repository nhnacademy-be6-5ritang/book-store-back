package com.nhnacademy.bookstoreback.book.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookstoreback.author.domain.entity.Author;
import com.nhnacademy.bookstoreback.author.service.AuthorService;
import com.nhnacademy.bookstoreback.book.domain.dto.request.CreateBookRequest;
import com.nhnacademy.bookstoreback.book.domain.dto.request.UpdateBookRequest;
import com.nhnacademy.bookstoreback.book.domain.dto.response.BookSearchResult;
import com.nhnacademy.bookstoreback.book.domain.dto.response.GetBookDetailResponse;
import com.nhnacademy.bookstoreback.book.domain.entity.Book;
import com.nhnacademy.bookstoreback.book.exception.BookAlreadyExistsException;
import com.nhnacademy.bookstoreback.book.exception.BookNotFoundException;
import com.nhnacademy.bookstoreback.book.repository.BookRepository;
import com.nhnacademy.bookstoreback.book.service.impl.BookServiceImpl;
import com.nhnacademy.bookstoreback.bookstatus.domain.entity.BookStatus;
import com.nhnacademy.bookstoreback.bookstatus.exception.BookStatusNotFoundException;
import com.nhnacademy.bookstoreback.bookstatus.repository.BookStatusRepository;
import com.nhnacademy.bookstoreback.bookstatus.service.BookStatusService;
import com.nhnacademy.bookstoreback.category.domain.entity.Category;
import com.nhnacademy.bookstoreback.category.repository.BookCategoryRepository;
import com.nhnacademy.bookstoreback.category.repository.CategoryRepository;
import com.nhnacademy.bookstoreback.category.service.CategoryService;
import com.nhnacademy.bookstoreback.image.domain.entity.Image;
import com.nhnacademy.bookstoreback.image.repository.BookImageRepository;
import com.nhnacademy.bookstoreback.image.repository.ImageRepository;
import com.nhnacademy.bookstoreback.image.service.BookImageService;
import com.nhnacademy.bookstoreback.image.service.CloudImageService;
import com.nhnacademy.bookstoreback.publisher.domain.entity.Publisher;
import com.nhnacademy.bookstoreback.publisher.service.PublisherService;
import com.nhnacademy.bookstoreback.tag.domain.entity.Tag;
import com.nhnacademy.bookstoreback.tag.repository.BookTagRepository;
import com.nhnacademy.bookstoreback.tag.repository.TagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class BookServiceImplTest {

    @InjectMocks
    private BookServiceImpl bookService;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private BookRepository bookRepository;

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
    private ImageRepository imageRepository;

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
        verify(bookImageRepository, times(2)).deleteAllByBookBookId(bookId);
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

    @Test
    void testCreateBook_Success() {
        // Given
        CreateBookRequest request = mock(CreateBookRequest.class);
        when(request.bookTitle()).thenReturn("New Book");
        when(request.authorName()).thenReturn("Author Name");
        when(request.publisherName()).thenReturn("Publisher Name");
        when(request.bookStatusName()).thenReturn("판매중");
        when(request.categories()).thenReturn(Collections.emptyList());
        when(request.tags()).thenReturn(Collections.emptyList());
        when(request.fileName()).thenReturn("https/test_image.png");

        when(bookRepository.existsByBookTitle(request.bookTitle())).thenReturn(false);

        Author mockAuthor = mock(Author.class);
        when(authorService.findOrCreateAuthor(request.authorName())).thenReturn(mockAuthor);

        Publisher mockPublisher = mock(Publisher.class);
        when(publisherService.findOrCreatePublisher(request.publisherName())).thenReturn(mockPublisher);

        BookStatus mockBookStatus = mock(BookStatus.class);
        when(bookStatusRepository.findByBookStatusName(request.bookStatusName()))
                .thenReturn(Optional.of(mockBookStatus));

        when(bookRepository.save(any(Book.class))).thenReturn(mock(Book.class));

        Image mockImage = mock(Image.class);
        when(imageRepository.save(any(Image.class))).thenReturn(mockImage);

        // When
        bookService.createBook(request);

        // Then
        verify(bookRepository, times(1)).existsByBookTitle(request.bookTitle());
        verify(authorService, times(1)).findOrCreateAuthor(request.authorName());
        verify(publisherService, times(1)).findOrCreatePublisher(request.publisherName());
        verify(bookStatusRepository, times(1)).findByBookStatusName(request.bookStatusName());
        verify(bookRepository, times(2)).save(any(Book.class)); // 최초 저장 및 이미지 추가 후 저장
    }

    @Test
    void testCreateBook_BookAlreadyExists() {
        // Given
        CreateBookRequest request = mock(CreateBookRequest.class);
        when(request.bookTitle()).thenReturn("Existing Book");

        when(bookRepository.existsByBookTitle(request.bookTitle())).thenReturn(true);

        // When & Then
        assertThrows(BookAlreadyExistsException.class, () -> bookService.createBook(request));
    }

    @Test
    void testCreateBook_BookStatusNotFound() {
        // Given
        CreateBookRequest request = mock(CreateBookRequest.class);
        when(request.bookTitle()).thenReturn("New Book");
        when(request.authorName()).thenReturn("Author Name");
        when(request.publisherName()).thenReturn("Publisher Name");
        when(request.bookStatusName()).thenReturn("Unknown Status");

        when(bookRepository.existsByBookTitle(request.bookTitle())).thenReturn(false);

        when(bookStatusRepository.findByBookStatusName(request.bookStatusName()))
                .thenReturn(Optional.empty());

        // When & Then
        assertThrows(BookStatusNotFoundException.class, () -> bookService.createBook(request));
    }

    @Test
    void testUpdateBookById_Success() {
        // Given
        Long bookId = 1L;
        Long categoryId = 1L;
        Long tagId = 1L;
        UpdateBookRequest request = new UpdateBookRequest(
                "1234567890123", // bookIsbn
                Collections.singletonList(categoryId), // categories
                Collections.singletonList(tagId), // tags
                "Updated Title", // bookTitle
                "Author Name", // authorName
                "Publisher Name", // publisherName
                new Date(), // bookPublishDate
                "판매중", // bookStatusName
                "Updated Description", // bookDescription
                10, // bookQuantity
                new BigDecimal("200.00"), // bookPrice
                new BigDecimal("180.00"), // bookSalePrice
                new BigDecimal("10.0"), // bookSalePercent
                "https/valid_image.png" // fileName
        );

        Book mockBook = mock(Book.class);
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(mockBook));

        Author mockAuthor = mock(Author.class);
        when(authorService.findOrCreateAuthor(request.authorName())).thenReturn(mockAuthor);

        Publisher mockPublisher = mock(Publisher.class);
        when(publisherService.findOrCreatePublisher(request.publisherName())).thenReturn(mockPublisher);

        BookStatus mockBookStatus = mock(BookStatus.class);
        when(bookStatusRepository.findByBookStatusName(request.bookStatusName()))
                .thenReturn(Optional.of(mockBookStatus));

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(mock(Category.class)));
        when(tagRepository.findById(tagId)).thenReturn(Optional.of(mock(Tag.class)));

        Image mockImage = mock(Image.class);
        when(imageRepository.save(any(Image.class))).thenReturn(mockImage);

        // When
        bookService.updateBookById(bookId, request);

        // Then
        verify(bookRepository, times(1)).findById(bookId);
        verify(mockBook, times(1)).updateBook(eq(mockAuthor), eq(mockPublisher), eq(mockBookStatus),
                eq("Updated Title"), eq("Updated Description"), eq(10), any(Date.class),
                eq("1234567890123"), eq(new BigDecimal("200.00")), eq(new BigDecimal("10.0")),
                eq(new BigDecimal("180.00")));
        verify(bookCategoryRepository, times(1)).deleteAllByBookBookId(bookId);
        verify(bookTagRepository, times(1)).deleteAllByBookBookId(bookId);
        verify(bookCategoryRepository, times(1)).save(any());
        verify(bookTagRepository, times(1)).save(any());
        verify(bookImageRepository, times(1)).deleteAllByBookBookId(bookId);
        verify(bookImageRepository, times(1)).save(any());
        verify(bookRepository, times(1)).save(mockBook);
    }

    @Test
    void testUpdateBookById_BookNotFound() {
        // Given
        Long bookId = 1L;
        UpdateBookRequest request = mock(UpdateBookRequest.class);

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(BookNotFoundException.class, () -> bookService.updateBookById(bookId, request));
    }

    @Test
    void testUpdateBookById_BookStatusNotFound() {
        // Given
        Long bookId = 1L;
        UpdateBookRequest request = mock(UpdateBookRequest.class);
        when(request.bookStatusName()).thenReturn("Unknown Status");

        Book mockBook = mock(Book.class);
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(mockBook));

        when(bookStatusRepository.findByBookStatusName(request.bookStatusName()))
                .thenReturn(Optional.empty());

        // When & Then
        assertThrows(BookStatusNotFoundException.class, () -> bookService.updateBookById(bookId, request));
    }

//	@Test
//	void testFindAllBooksByCategoryName() {
//		// Given
//		String categoryName = "Fiction";
//		Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "bookTitle"));
//		Book book1 = mock(Book.class);
//		Book book2 = mock(Book.class);
//
//		// Mocking Author object
//		Author mockAuthor = mock(Author.class);
//		when(mockAuthor.getAuthorName()).thenReturn("John Doe");
//
//		// Mocking Publisher object
//		Publisher mockPublisher = mock(Publisher.class);
//		when(mockPublisher.getPublisherName()).thenReturn("Test Publisher");
//
//		// Mocking BookStatus object
//		BookStatus mockBookStatus = mock(BookStatus.class);
//		when(mockBookStatus.getBookStatusName()).thenReturn("판매중");
//
//		// Setting up Book mock objects to return the mocked Author, Publisher, and BookStatus
//		when(book1.getAuthor()).thenReturn(mockAuthor);
//		when(book1.getPublisher()).thenReturn(mockPublisher);
//		when(book1.getBookStatus()).thenReturn(mockBookStatus);
//
//		when(book2.getAuthor()).thenReturn(mockAuthor);
//		when(book2.getPublisher()).thenReturn(mockPublisher);
//		when(book2.getBookStatus()).thenReturn(mockBookStatus);
//
//		List<Book> books = Arrays.asList(book1, book2);
//		Page<Book> bookPage = new PageImpl<>(books, pageable, books.size());
//
//		when(bookRepository.findAllByBookCategories_Category_CategoryName(pageable, categoryName))
//			.thenReturn(bookPage);
//
//		// When
//		Page<GetBookDetailResponse> result = bookService.findAllBooksByCategoryName(pageable, categoryName);
//
//		// Then
//		assertNotNull(result);
//		assertEquals(2, result.getTotalElements());
//		verify(bookRepository, times(1)).findAllByBookCategories_Category_CategoryName(pageable, categoryName);
//	}

}