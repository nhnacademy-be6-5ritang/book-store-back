package com.nhnacademy.bookstoreback.tag.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nhnacademy.bookstoreback.tag.domain.dto.respnse.TagDto;
import com.nhnacademy.bookstoreback.tag.domain.entity.BookTag;
import com.nhnacademy.bookstoreback.tag.domain.entity.Tag;
import com.nhnacademy.bookstoreback.tag.exception.TagAlreadyExistsException;
import com.nhnacademy.bookstoreback.tag.exception.TagNotFoundException;
import com.nhnacademy.bookstoreback.tag.repository.BookTagRepository;
import com.nhnacademy.bookstoreback.tag.repository.TagRepository;
import com.nhnacademy.bookstoreback.tag.service.TagService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class TagServiceImpl implements TagService {
	private final TagRepository tagRepository;
	private final BookTagRepository bookTagRepository;

	@Transactional(readOnly = true)
	@Override
	public List<TagDto> getTags() {
		return tagRepository.findAll().stream().map(TagDto::fromEntity).toList();
	}

	@Transactional(readOnly = true)
	@Override
	public Page<TagDto> getTags(Pageable pageable) {
		int page = Math.max(pageable.getPageNumber() - 1, 0);
		int pageSize = pageable.getPageSize();
		return tagRepository.findAll(PageRequest.of(page, pageSize, Sort.by(Sort.Direction.ASC, "tagId")))
			.map(TagDto::fromEntity);
	}

	@Transactional(readOnly = true)
	@Override
	public List<TagDto> getTagsByTagId(Long bookId) {
		List<BookTag> bookTags = bookTagRepository.findAllByBookBookId(bookId);
		List<Tag> tags = bookTags.stream().map(BookTag::getTag).toList();
		return tags.stream().map(TagDto::fromEntity).toList();
	}

	@Transactional(readOnly = true)
	@Override
	public TagDto getTag(Long tagId) {
		Tag tag = tagRepository.findById(tagId).orElseThrow(() -> new TagNotFoundException(tagId));
		return TagDto.fromEntity(tag);
	}

	@Override
	public TagDto createTag(TagDto request) {
		if (tagRepository.existsByTagName(request.tagName())) {
			throw new TagAlreadyExistsException(request.tagName());
		}
		return TagDto.fromEntity(tagRepository.save(Tag.toEntity(request)));
	}

	@Override
	public TagDto updateTag(Long tagId, TagDto request) {
		Tag tag = tagRepository.findById(tagId).orElseThrow(() -> new TagNotFoundException(tagId));

		if (tagRepository.existsByTagName(request.tagName())) {
			throw new TagAlreadyExistsException(request.tagName());
		}
		tag.updateTagName(request.tagName());
		return TagDto.fromEntity(tag);
	}

	@Override
	public void deleteTag(Long tagId) {
		tagRepository.findById(tagId).orElseThrow(() -> new TagNotFoundException(tagId));
		bookTagRepository.deleteAllByTagTagId(tagId);
		tagRepository.deleteById(tagId);
	}
}
