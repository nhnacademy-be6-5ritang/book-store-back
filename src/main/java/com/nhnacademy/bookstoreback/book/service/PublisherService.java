package com.nhnacademy.bookstoreback.book.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nhnacademy.bookstoreback.book.domain.entity.Publisher;
import com.nhnacademy.bookstoreback.book.repository.PublisherRepository;

import lombok.RequiredArgsConstructor;

/**
 * 출판사 Service
 *
 * @author 김기욱
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class PublisherService {
	private final PublisherRepository publisherRepository;

	@Transactional
	public Publisher findOrCreatePublisher(String publisherName) {
		return publisherRepository.findByPublisherName(publisherName).orElseGet(() -> {
			Publisher newPublisher = new Publisher();
			newPublisher.setPublisherName(publisherName);
			return publisherRepository.save(newPublisher);
		});
	}
}
