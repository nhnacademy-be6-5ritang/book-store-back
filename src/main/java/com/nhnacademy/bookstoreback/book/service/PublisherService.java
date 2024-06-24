package com.nhnacademy.bookstoreback.book.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nhnacademy.bookstoreback.book.domain.entity.Publisher;
import com.nhnacademy.bookstoreback.book.repository.PublisherRepository;

/**
 * @author 김기욱
 * @version 1.0
 */
@Service
public class PublisherService {

	private final PublisherRepository publisherRepository;

	@Autowired
	public PublisherService(PublisherRepository publisherRepository) {
		this.publisherRepository = publisherRepository;
	}

	@Transactional
	public Publisher findOrCreatePublisher(String publisherName) {
		return publisherRepository.findByPublisherName(publisherName).orElseGet(() -> {
			Publisher newPublisher = new Publisher();
			newPublisher.setPublisherName(publisherName);
			return publisherRepository.save(newPublisher);
		});
	}
}
