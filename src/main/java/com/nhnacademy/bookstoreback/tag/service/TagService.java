package com.nhnacademy.bookstoreback.tag.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nhnacademy.bookstoreback.global.exception.AlreadyExistsException;
import com.nhnacademy.bookstoreback.global.exception.NotFoundException;
import com.nhnacademy.bookstoreback.global.exception.payload.ErrorStatus;
import com.nhnacademy.bookstoreback.tag.domain.dto.respnse.TagDto;
import com.nhnacademy.bookstoreback.tag.domain.entity.BookTag;
import com.nhnacademy.bookstoreback.tag.domain.entity.Tag;
import com.nhnacademy.bookstoreback.tag.repository.BookTagRepository;
import com.nhnacademy.bookstoreback.tag.repository.TagRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class TagService {
	private final TagRepository tagRepository;
	private final BookTagRepository bookTagRepository;

	@Transactional(readOnly = true)
	public List<TagDto> getTags() {
		return tagRepository.findAll().stream().map(TagDto::fromEntity).toList();
	}

	@Transactional(readOnly = true)
	public List<TagDto> getTagsByTagId(Long bookId) {
		List<BookTag> bookTags = bookTagRepository.findAllByBookBookId(bookId);
		List<Tag> tags = bookTags.stream().map(BookTag::getTag).toList();
		return tags.stream().map(TagDto::fromEntity).toList();
	}

	@Transactional(readOnly = true)
	public TagDto getTag(Long tagId) {
		Tag tag = tagRepository.findById(tagId).orElseThrow(() -> {
			String errorMessage = String.format("해당 태그 '%d'는 존재하지 않는 태그 입니다.", tagId);
			ErrorStatus errorStatus = ErrorStatus.from(errorMessage, HttpStatus.NOT_FOUND, LocalDateTime.now());
			return new NotFoundException(errorStatus);
		});

		return TagDto.fromEntity(tag);
	}

	public TagDto createTag(TagDto request) {
		if (tagRepository.existsByTagName(request.tagName())) {
			String errorMessage = String.format("해당 태그 '%s'는 이미 존재 하는 태그 입니다.", request.tagName());
			ErrorStatus errorStatus = ErrorStatus.from(errorMessage, HttpStatus.NOT_FOUND, LocalDateTime.now());
			throw new AlreadyExistsException(errorStatus);
		}

		return TagDto.fromEntity(tagRepository.save(Tag.toEntity(request)));
	}

	public TagDto updateTag(Long tagId, TagDto request) {
		Tag tag = tagRepository.findById(tagId).orElseThrow(() -> {
			String errorMessage = String.format("해당 태그 '%d'는 존재하지 않는 태그 입니다.", tagId);
			ErrorStatus errorStatus = ErrorStatus.from(errorMessage, HttpStatus.NOT_FOUND, LocalDateTime.now());
			return new NotFoundException(errorStatus);
		});

		if (tagRepository.existsByTagName(request.tagName())) {
			String errorMessage = String.format("해당 태그 '%s'는 이미 존재 하는 태그 입니다.", request.tagName());
			ErrorStatus errorStatus = ErrorStatus.from(errorMessage, HttpStatus.NOT_FOUND, LocalDateTime.now());
			throw new AlreadyExistsException(errorStatus);
		}

		tag.updateTagName(request.tagName());

		tagRepository.save(tag);

		return TagDto.fromEntity(tag);
	}

	public void deleteTag(Long tagId) {
		tagRepository.findById(tagId).orElseThrow(() -> {
			String errorMessage = String.format("해당 태그 '%d'는 존재하지 않는 태그 입니다.", tagId);
			ErrorStatus errorStatus = ErrorStatus.from(errorMessage, HttpStatus.NOT_FOUND, LocalDateTime.now());
			return new NotFoundException(errorStatus);
		});

		tagRepository.deleteById(tagId);
	}
}
