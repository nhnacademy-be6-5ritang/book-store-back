package com.nhnacademy.bookstoreback.tag.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

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

import com.nhnacademy.bookstoreback.book.domain.entity.Book;
import com.nhnacademy.bookstoreback.tag.domain.dto.respnse.TagDto;
import com.nhnacademy.bookstoreback.tag.domain.entity.BookTag;
import com.nhnacademy.bookstoreback.tag.domain.entity.Tag;
import com.nhnacademy.bookstoreback.tag.exception.TagAlreadyExistsException;
import com.nhnacademy.bookstoreback.tag.exception.TagNotFoundException;
import com.nhnacademy.bookstoreback.tag.repository.BookTagRepository;
import com.nhnacademy.bookstoreback.tag.repository.TagRepository;

class TagServiceImplTest {

	@InjectMocks
	private TagServiceImpl tagService;

	@Mock
	private TagRepository tagRepository;

	@Mock
	private BookTagRepository bookTagRepository;

	private Tag tag;
	private TagDto tagDto;
	private Book book;
	private BookTag bookTag;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);

		tag = new Tag("SampleTag");
		tagDto = new TagDto(1L, "SampleTag");

		book = mock(Book.class);
		bookTag = new BookTag(book, tag);
	}

	@Test
	void testGetTags() {
		given(tagRepository.findAll()).willReturn(Collections.singletonList(tag));

		List<TagDto> result = tagService.getTags();

		assertEquals(1, result.size());
		assertEquals(tagDto.tagName(), result.get(0).tagName());
	}

	@Test
	void testGetTagsPageable() {
		Pageable pageable = PageRequest.of(0, 10);
		Page<Tag> tagPage = new PageImpl<>(Collections.singletonList(tag), pageable, 1);
		given(tagRepository.findAll(any(Pageable.class))).willReturn(tagPage);

		Page<TagDto> result = tagService.getTags(pageable);

		assertEquals(1, result.getTotalElements());
		assertEquals(tagDto.tagName(), result.getContent().get(0).tagName());
	}

	@Test
	void testGetTagsByTagId() {
		given(bookTagRepository.findAllByBookBookId(anyLong())).willReturn(Collections.singletonList(bookTag));

		List<TagDto> result = tagService.getTagsByTagId(1L);

		assertEquals(1, result.size());
		assertEquals(tagDto.tagName(), result.get(0).tagName());
	}

	@Test
	void testGetTag() {
		given(tagRepository.findById(anyLong())).willReturn(Optional.of(tag));

		TagDto result = tagService.getTag(1L);

		assertEquals(tagDto.tagName(), result.tagName());
	}

	@Test
	void testCreateTag() {
		given(tagRepository.existsByTagName(any())).willReturn(false);
		given(tagRepository.save(any(Tag.class))).willReturn(tag);

		TagDto result = tagService.createTag(tagDto);

		assertEquals(tagDto.tagName(), result.tagName());
		verify(tagRepository).save(any(Tag.class));
	}

	@Test
	void testCreateTagTagAlreadyExistsException() {
		given(tagRepository.existsByTagName(any())).willReturn(true);

		assertThrows(TagAlreadyExistsException.class, () -> tagService.createTag(tagDto));
	}

	@Test
	void testUpdateTag() {
		given(tagRepository.findById(anyLong())).willReturn(Optional.of(tag));
		given(tagRepository.existsByTagName(any())).willReturn(false);

		TagDto result = tagService.updateTag(1L, tagDto);

		assertEquals(tagDto.tagName(), result.tagName());
	}

	@Test
	void testUpdateTagTagAlreadyExistsException() {
		given(tagRepository.findById(anyLong())).willReturn(Optional.of(tag));
		given(tagRepository.existsByTagName(any())).willReturn(true);

		assertThrows(TagAlreadyExistsException.class, () -> tagService.updateTag(1L, tagDto));
	}

	@Test
	void testDeleteTag() {
		given(tagRepository.findById(anyLong())).willReturn(Optional.of(tag));

		tagService.deleteTag(1L);

		verify(tagRepository).deleteById(anyLong());
		verify(bookTagRepository).deleteAllByTagTagId(anyLong());
	}

	@Test
	void testDeleteTagTagNotFoundException() {
		given(tagRepository.findById(anyLong())).willReturn(Optional.empty());

		assertThrows(TagNotFoundException.class, () -> tagService.deleteTag(1L));
	}
}