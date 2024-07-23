package com.nhnacademy.bookstoreback.author.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

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

import com.nhnacademy.bookstoreback.author.domain.dto.respnse.AuthorDto;
import com.nhnacademy.bookstoreback.author.domain.entity.Author;
import com.nhnacademy.bookstoreback.author.exception.AuthorAlreadyExistsException;
import com.nhnacademy.bookstoreback.author.exception.AuthorNotFoundException;
import com.nhnacademy.bookstoreback.author.repository.AuthorRepository;
import com.nhnacademy.bookstoreback.author.service.impl.AuthorServiceImpl;

class AuthorServiceImplTest {

	@Mock
	private AuthorRepository authorRepository;

	@InjectMocks
	private AuthorServiceImpl authorService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testFindOrCreateAuthor_CreatesNewAuthor() {
		when(authorRepository.findByAuthorName(anyString())).thenReturn(Optional.empty());
		when(authorRepository.save(any(Author.class))).thenReturn(new Author(1L, "New Author"));

		Author author = authorService.findOrCreateAuthor("New Author");

		assertNotNull(author);
		assertEquals("New Author", author.getAuthorName());
		verify(authorRepository, times(1)).save(any(Author.class));
	}

	@Test
	void testFindOrCreateAuthor_ReturnsExistingAuthor() {
		Author existingAuthor = new Author(1L, "Existing Author");
		when(authorRepository.findByAuthorName(anyString())).thenReturn(Optional.of(existingAuthor));

		Author author = authorService.findOrCreateAuthor("Existing Author");

		assertNotNull(author);
		assertEquals("Existing Author", author.getAuthorName());
		verify(authorRepository, never()).save(any(Author.class));
	}

	@Test
	void testGetAuthors() {
		List<Author> authors = List.of(new Author(1L, "Author 1"), new Author(2L, "Author 2"));
		when(authorRepository.findAll()).thenReturn(authors);

		List<AuthorDto> authorDtos = authorService.getAuthors();

		assertEquals(2, authorDtos.size());
		assertEquals("Author 1", authorDtos.get(0).authorName());
		verify(authorRepository, times(1)).findAll();
	}

	@Test
	void testGetAuthors_WithPagination() {
		Pageable pageable = PageRequest.of(0, 10);
		List<Author> authors = List.of(new Author(1L, "Author 1"), new Author(2L, "Author 2"));
		Page<Author> authorPage = new PageImpl<>(authors, pageable, authors.size());
		when(authorRepository.findAll(any(Pageable.class))).thenReturn(authorPage);

		Page<AuthorDto> authorDtos = authorService.getAuthors(pageable);

		assertEquals(2, authorDtos.getContent().size());
		assertEquals("Author 1", authorDtos.getContent().get(0).authorName());
		verify(authorRepository, times(1)).findAll(any(Pageable.class));
	}

	@Test
	void testGetAuthor_Found() {
		Author author = new Author(1L, "Author 1");
		when(authorRepository.findById(anyLong())).thenReturn(Optional.of(author));

		AuthorDto authorDto = authorService.getAuthor(1L);

		assertNotNull(authorDto);
		assertEquals("Author 1", authorDto.authorName());
		verify(authorRepository, times(1)).findById(anyLong());
	}

	@Test
	void testGetAuthor_NotFound() {
		when(authorRepository.findById(anyLong())).thenReturn(Optional.empty());

		assertThrows(AuthorNotFoundException.class, () -> authorService.getAuthor(1L));

		verify(authorRepository, times(1)).findById(anyLong());
	}

	@Test
	void testCreateAuthor() {
		when(authorRepository.existsByAuthorName(anyString())).thenReturn(false);
		when(authorRepository.save(any(Author.class))).thenReturn(new Author(1L, "New Author"));

		AuthorDto authorDto = new AuthorDto(null, "New Author");
		authorService.createAuthor(authorDto);

		verify(authorRepository, times(1)).save(any(Author.class));
	}

	@Test
	void testCreateAuthor_AlreadyExists() {
		when(authorRepository.existsByAuthorName(anyString())).thenReturn(true);

		AuthorDto authorDto = new AuthorDto(null, "Existing Author");

		assertThrows(AuthorAlreadyExistsException.class, () -> authorService.createAuthor(authorDto));

		verify(authorRepository, never()).save(any(Author.class));
	}

	@Test
	void testUpdateAuthor() {
		Author existingAuthor = new Author(1L, "Old Author");
		when(authorRepository.findById(anyLong())).thenReturn(Optional.of(existingAuthor));
		when(authorRepository.existsByAuthorName(anyString())).thenReturn(false);

		AuthorDto authorDto = new AuthorDto(1L, "Updated Author");
		authorService.updateAuthor(1L, authorDto);

		assertEquals("Updated Author", existingAuthor.getAuthorName());
		verify(authorRepository, times(1)).findById(anyLong());
		verify(authorRepository, never()).save(any(Author.class));
	}

	@Test
	void testUpdateAuthor_NotFound() {
		when(authorRepository.findById(anyLong())).thenReturn(Optional.empty());

		AuthorDto authorDto = new AuthorDto(1L, "Updated Author");

		assertThrows(AuthorNotFoundException.class, () -> authorService.updateAuthor(1L, authorDto));

		verify(authorRepository, times(1)).findById(anyLong());
		verify(authorRepository, never()).save(any(Author.class));
	}

	@Test
	void testUpdateAuthor_AlreadyExists() {
		Author existingAuthor = new Author(1L, "Old Author");
		when(authorRepository.findById(anyLong())).thenReturn(Optional.of(existingAuthor));
		when(authorRepository.existsByAuthorName(anyString())).thenReturn(true);

		AuthorDto authorDto = new AuthorDto(1L, "Existing Author");

		assertThrows(AuthorAlreadyExistsException.class, () -> authorService.updateAuthor(1L, authorDto));

		verify(authorRepository, times(1)).findById(anyLong());
		verify(authorRepository, never()).save(any(Author.class));
	}

	@Test
	void testDeleteAuthor() {
		when(authorRepository.findById(anyLong())).thenReturn(Optional.of(new Author(1L, "Author")));

		authorService.deleteAuthor(1L);

		verify(authorRepository, times(1)).findById(anyLong());
		verify(authorRepository, times(1)).deleteById(anyLong());
	}

	@Test
	void testDeleteAuthor_NotFound() {
		when(authorRepository.findById(anyLong())).thenReturn(Optional.empty());

		assertThrows(AuthorNotFoundException.class, () -> authorService.deleteAuthor(1L));

		verify(authorRepository, times(1)).findById(anyLong());
		verify(authorRepository, never()).deleteById(anyLong());
	}
}
