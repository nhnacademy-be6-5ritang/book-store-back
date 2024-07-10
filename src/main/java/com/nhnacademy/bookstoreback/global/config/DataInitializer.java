package com.nhnacademy.bookstoreback.global.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.nhnacademy.bookstoreback.book.controller.BookController;
import com.nhnacademy.bookstoreback.image.controller.BookImageController;
import com.nhnacademy.bookstoreback.image.controller.CoverImageController;

/**
 * @version 1.0
 * @author 김기욱
 *
 * 초기 데이터를 로드하는 클래스 (베스트 셀러 도서 정보를 로드)
 * 국내도서 50권, 외국도서 50권을 로드하여 저장
 * 도서정보, 작가, 출판사, 도서상태, 카테고리 저장
 * 이미지 다운로드 및 매핑 + NHN Cloud 에 이미지 업로드
 * NHN Cloud 에 업로드된 이미지 URL 로 매핑
 * 이미지 매핑 정보를 book_images 테이블에 저장
 */
@Component
public class DataInitializer implements CommandLineRunner {

	private final BookController bookController;
	private final CoverImageController coverImageController;
	private final BookImageController bookImageController;

	@Autowired
	public DataInitializer(BookController bookController,
		CoverImageController coverImageController,
		BookImageController bookImageController) {
		this.bookController = bookController;
		this.coverImageController = coverImageController;
		this.bookImageController = bookImageController;
	}

	@Override
	public void run(String... args) throws Exception {
		// ResponseEntity<String> bookFetchResponse = bookController.fetchAndSaveBooks(50L);
		// if (bookFetchResponse.getStatusCode().is2xxSuccessful()) {
		// 	System.out.println("Books fetched and saved successfully.");
		// } else {
		// 	System.out.println("Failed to fetch and save books: " + bookFetchResponse.getBody());
		// 	return; // 데이터 로드 실패 시 다음 작업을 수행하지 않음
		// }
	}
}
