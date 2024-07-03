package com.nhnacademy.bookstoreback.publisher.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nhnacademy.bookstoreback.publisher.domain.dto.respnse.PublisherDto;
import com.nhnacademy.bookstoreback.publisher.domain.entity.Publisher;
import com.nhnacademy.bookstoreback.publisher.exception.PublisherAlreadyExistsException;
import com.nhnacademy.bookstoreback.publisher.exception.PublisherNotFoundException;
import com.nhnacademy.bookstoreback.publisher.repository.PublisherRepository;
import com.nhnacademy.bookstoreback.publisher.service.PublisherService;

import lombok.RequiredArgsConstructor;

/**
 * 출판사 Service
 *
 * @author 김기욱
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Transactional
public class PublisherServiceImpl implements PublisherService {
	private final PublisherRepository publisherRepository;

	@Override
	public Publisher findOrCreatePublisher(String publisherName) {
		return publisherRepository.findByPublisherName(publisherName).orElseGet(() -> {
			Publisher newPublisher = new Publisher();
			newPublisher.setPublisherName(publisherName);
			return publisherRepository.save(newPublisher);
		});
	}

	@Transactional(readOnly = true)
	@Override
	public List<PublisherDto> getPublishers() {
		return publisherRepository.findAll().stream().map(PublisherDto::fromEntity).toList();
	}

	@Transactional(readOnly = true)
	@Override
	public Page<PublisherDto> getPublishers(Pageable pageable) {
		int page = pageable.getPageNumber() - 1;
		int pageSize = pageable.getPageSize();
		return publisherRepository.findAll(PageRequest.of(page, pageSize, Sort.by(Sort.Direction.ASC, "publisherId")))
			.map(PublisherDto::fromEntity);
	}

	@Transactional(readOnly = true)
	@Override
	public PublisherDto getPublisher(Long publisherId) {
		Publisher publisher = publisherRepository.findById(publisherId)
			.orElseThrow(() -> new PublisherNotFoundException(publisherId));
		return PublisherDto.fromEntity(publisher);
	}

	@Override
	public PublisherDto createPublisher(PublisherDto request) {
		if (publisherRepository.existsByPublisherName(request.publisherName())) {
			throw new PublisherAlreadyExistsException(request.publisherName());
		}
		return PublisherDto.fromEntity(publisherRepository.save(Publisher.toEntity(request)));
	}

	@Override
	public PublisherDto updatePublisher(Long publisherId, PublisherDto request) {
		Publisher publisher = publisherRepository.findById(publisherId)
			.orElseThrow(() -> new PublisherNotFoundException(publisherId));

		if (publisherRepository.existsByPublisherName(request.publisherName())) {
			throw new PublisherAlreadyExistsException(request.publisherName());
		}
		publisher.updatePublisherName(request.publisherName());
		return PublisherDto.fromEntity(publisher);
	}

	@Override
	public void deletePublisher(Long publisherId) {
		publisherRepository.findById(publisherId).orElseThrow(() -> new PublisherNotFoundException(publisherId));
		publisherRepository.deleteById(publisherId);
	}

}
