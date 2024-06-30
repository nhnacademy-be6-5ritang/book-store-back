package com.nhnacademy.bookstoreback.publisher.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nhnacademy.bookstoreback.global.exception.AlreadyExistsException;
import com.nhnacademy.bookstoreback.global.exception.NotFoundException;
import com.nhnacademy.bookstoreback.global.exception.payload.ErrorStatus;
import com.nhnacademy.bookstoreback.publisher.domain.dto.respnse.PublisherDto;
import com.nhnacademy.bookstoreback.publisher.domain.entity.Publisher;
import com.nhnacademy.bookstoreback.publisher.repository.PublisherRepository;

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
public class PublisherService {
	private final PublisherRepository publisherRepository;

	public Publisher findOrCreatePublisher(String publisherName) {
		return publisherRepository.findByPublisherName(publisherName).orElseGet(() -> {
			Publisher newPublisher = new Publisher();
			newPublisher.setPublisherName(publisherName);
			return publisherRepository.save(newPublisher);
		});
	}

	@Transactional(readOnly = true)
	public List<PublisherDto> getPublishers() {
		return publisherRepository.findAll().stream().map(PublisherDto::fromEntity).toList();
	}

	@Transactional(readOnly = true)
	public PublisherDto getPublisher(Long publisherId) {
		Publisher publisher = publisherRepository.findById(publisherId).orElseThrow(() -> {
			String errorMessage = String.format("해당 출판사 '%d'는 존재하지 않는 출판사 입니다.", publisherId);
			ErrorStatus errorStatus = ErrorStatus.from(errorMessage, HttpStatus.NOT_FOUND, LocalDateTime.now());
			return new NotFoundException(errorStatus);
		});

		return PublisherDto.fromEntity(publisher);
	}

	public PublisherDto createPublisher(PublisherDto request) {
		if (publisherRepository.existsByPublisherName(request.publisherName())) {
			String errorMessage = String.format("해당 출판사 '%s'는 이미 존재 하는 출판사 입니다.", request.publisherName());
			ErrorStatus errorStatus = ErrorStatus.from(errorMessage, HttpStatus.NOT_FOUND, LocalDateTime.now());
			throw new AlreadyExistsException(errorStatus);
		}

		return PublisherDto.fromEntity(publisherRepository.save(Publisher.toEntity(request)));
	}

	public PublisherDto updatePublisher(Long publisherId, PublisherDto request) {
		Publisher publisher = publisherRepository.findById(publisherId).orElseThrow(() -> {
			String errorMessage = String.format("해당 출판사 '%d'는 존재하지 않는 출판사 입니다.", publisherId);
			ErrorStatus errorStatus = ErrorStatus.from(errorMessage, HttpStatus.NOT_FOUND, LocalDateTime.now());
			return new NotFoundException(errorStatus);
		});

		if (publisherRepository.existsByPublisherName(request.publisherName())) {
			String errorMessage = String.format("해당 출판사 '%s'는 이미 존재 하는 출판사 입니다.", request.publisherName());
			ErrorStatus errorStatus = ErrorStatus.from(errorMessage, HttpStatus.NOT_FOUND, LocalDateTime.now());
			throw new AlreadyExistsException(errorStatus);
		}

		publisher.updatePublisherName(request.publisherName());

		publisherRepository.save(publisher);

		return PublisherDto.fromEntity(publisher);

	}

	public void deletePublisher(Long publisherId) {
		publisherRepository.findById(publisherId).orElseThrow(() -> {
			String errorMessage = String.format("해당 출판사 '%d'는 존재하지 않는 출판사 입니다.", publisherId);
			ErrorStatus errorStatus = ErrorStatus.from(errorMessage, HttpStatus.NOT_FOUND, LocalDateTime.now());
			return new NotFoundException(errorStatus);
		});

		publisherRepository.deleteById(publisherId);
	}

}
