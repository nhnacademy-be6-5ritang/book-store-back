package com.nhnacademy.bookstoreback.bookcart.controller;

import org.springframework.web.bind.annotation.RestController;

import com.nhnacademy.bookstoreback.bookcart.service.BookCartService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class BookCartController {
	private final BookCartService bookCartService;
}
