package com.nhnacademy.bookstoreback.image.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nhnacademy.bookstoreback.book.domain.entity.Book;
import com.nhnacademy.bookstoreback.book.domain.entity.BookImage;
import com.nhnacademy.bookstoreback.image.domain.entity.Image;
import com.nhnacademy.bookstoreback.image.repository.BookImageRepository;
import com.nhnacademy.bookstoreback.image.repository.ImageRepository;

import lombok.RequiredArgsConstructor;

/**
 * @author 김기욱
 * 책과 이미지를 매핑하는 작업을 처리하는 서비스 클래스입니다.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class BookImageService {
	private final ImageRepository imageRepository;
	private final BookImageRepository bookImageRepository;

	/**
	 * 주어진 책과 이미지를 매핑하여 저장합니다.
	 *
	 * @param book      책 객체
	 * @param imageUrl  책과 연관될 이미지의 URL. URL 이 {@code null}이거나 비어 있을 경우, 기본 URL 을 사용하여 이미지를 생성합니다.
	 */
	public void mapImageToBook(Book book, String imageUrl) {
		String fileName = book.getBookTitle() + ".jpg";
		Image image;
		if (imageUrl == null || imageUrl.isEmpty()) {
			image = new Image("null.jpg",
				"http://image.toast.com/aaaacuf/5ritang/books/null.jpg"); // 기본 Url 을 사용
		} else {
			image = new Image(fileName, imageUrl);
		}

		imageRepository.save(image);
		bookImageRepository.save(new BookImage(book, image));
	}
}
