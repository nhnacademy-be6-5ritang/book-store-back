package com.nhnacademy.bookstoreback.global.util;

import com.nhnacademy.bookstoreback.upload.exception.ParserException;

/**
 * @author 이경헌
 * 이미지 관련 작업을 위한 유틸리티 클래스입니다.
 */
public class ImageUtil {
	private ImageUtil() {
	}

	public static String fileNameParser(String imageUrl) {
		if (imageUrl == null || imageUrl.isEmpty()) {
			throw new ParserException();
		}

		// 마지막 슬래시의 인덱스 찾기
		int lastSlashIndex = imageUrl.lastIndexOf('/');

		// 마지막 슬래시 뒤의 문자열 추출
		if (lastSlashIndex != -1 && lastSlashIndex < imageUrl.length() - 1) {
			return imageUrl.substring(lastSlashIndex + 1);
		} else {
			throw new ParserException();
		}
	}
}
