package com.nhnacademy.bookstoreback.bookcart.service.impl;

import org.springframework.stereotype.Service;

import com.nhnacademy.bookstoreback.bookcart.repository.BookCartRepository;
import com.nhnacademy.bookstoreback.bookcart.service.BookCartService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookCartServiceImpl implements BookCartService {
	private final BookCartRepository bookCartRepository;
}
