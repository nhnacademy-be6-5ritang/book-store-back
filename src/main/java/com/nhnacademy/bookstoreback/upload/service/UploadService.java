package com.nhnacademy.bookstoreback.upload.service;

import org.springframework.web.multipart.MultipartFile;

import com.nhnacademy.bookstoreback.upload.exception.FileUploadException;

/**
 * @author 이경헌
 * 파일 업로드 서비스 인터페이스입니다.
 */
public interface UploadService {

	/**
	 * 주어진 MultipartFile을 업로드하고 업로드된 이미지의 URL을 반환합니다.
	 *
	 * @param file 업로드할 파일 (MultipartFile)
	 * @return 업로드된 이미지의 URL
	 * @throws FileUploadException 업로드 중 발생한 예외
	 */
	String upload(MultipartFile file) throws FileUploadException;

	/**
	 * 이미지 업로드 API의 응답 본문에서 이미지 URL을 추출합니다.
	 *
	 * @param responseBody 이미지 업로드 API의 응답 본문
	 * @return 응답 본문에서 추출한 업로드된 이미지의 URL
	 */
	String extractImageUrlFromResponse(String responseBody);

	/**
	 * 파일의 컨텐츠 타입이 지원하는 이미지 확장자인지 확인합니다. (jpg, jpeg, png)
	 *
	 * @param fileContentType 업로드되는 파일의 컨텐츠 타입
	 */
	void checkFileExtension(String fileContentType);
}