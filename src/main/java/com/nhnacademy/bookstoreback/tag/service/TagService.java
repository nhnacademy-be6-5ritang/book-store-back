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
import com.nhnacademy.bookstoreback.tag.domain.entity.Tag;
import com.nhnacademy.bookstoreback.tag.repository.TagRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class TagService {
	private final TagRepository tagRepository;

	public List<TagDto> getTags() {
		return tagRepository.findAll().stream().map(TagDto::fromEntity).toList();
	}

	public TagDto getTag(Long tagId) {
		Tag tag = tagRepository.findById(tagId).orElseThrow(() -> {
			String errorMessage = String.format("해당 태그 '%d'는 존재하지 않는 테그 입니다.", tagId);
			ErrorStatus errorStatus = ErrorStatus.from(errorMessage, HttpStatus.NOT_FOUND, LocalDateTime.now());
			return new NotFoundException(errorStatus);
		});

		return TagDto.fromEntity(tag);
	}

	public TagDto updateTag(Long tagId, TagDto tagDto) {
		Tag tag = tagRepository.findById(tagId).orElseThrow(() -> {
			String errorMessage = String.format("해당 태그 '%s'는 존재하지 않는 테그 입니다.", tagDto.tagName());
			ErrorStatus errorStatus = ErrorStatus.from(errorMessage, HttpStatus.NOT_FOUND, LocalDateTime.now());
			return new NotFoundException(errorStatus);
		});

		if (tagRepository.existsByTagName(tagDto.tagName())) {
			String errorMessage = String.format("해당 태그 '%s'는 이미 존재 하는 테그 입니다.", tagDto.tagName());
			ErrorStatus errorStatus = ErrorStatus.from(errorMessage, HttpStatus.NOT_FOUND, LocalDateTime.now());
			throw new AlreadyExistsException(errorStatus);
		}

		tag.updateTagName(tagDto.tagName());

		tagRepository.save(tag);

		return TagDto.fromEntity(tag);
	}

	public TagDto createTag(TagDto tagDto) {
		if (tagRepository.existsByTagName(tagDto.tagName())) {
			String errorMessage = String.format("해당 태그 '%s'는 이미 존재 하는 테그 입니다.", tagDto.tagName());
			ErrorStatus errorStatus = ErrorStatus.from(errorMessage, HttpStatus.NOT_FOUND, LocalDateTime.now());
			throw new AlreadyExistsException(errorStatus);
		}

		return TagDto.fromEntity(tagRepository.save(Tag.toEntity(tagDto)));
	}

	public void deleteTag(Long tagId) {
		tagRepository.findById(tagId).orElseThrow(() -> {
			String errorMessage = String.format("해당 태그 '%d'는 존재하지 않는 테그 입니다.", tagId);
			ErrorStatus errorStatus = ErrorStatus.from(errorMessage, HttpStatus.NOT_FOUND, LocalDateTime.now());
			return new NotFoundException(errorStatus);
		});

		tagRepository.deleteById(tagId);
	}
}
