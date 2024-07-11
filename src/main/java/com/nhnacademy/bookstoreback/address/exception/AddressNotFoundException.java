package com.nhnacademy.bookstoreback.address.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import com.nhnacademy.bookstoreback.global.exception.NotFoundException;
import com.nhnacademy.bookstoreback.global.exception.payload.ErrorStatus;

public class AddressNotFoundException extends NotFoundException {
	public AddressNotFoundException(Long addressId, Long userId) {
		super(ErrorStatus.from(
			String.format("해당 주소를 찾을 수 없습니다. addressId: %d, userId: %d", addressId, userId),
			HttpStatus.NOT_FOUND,
			LocalDateTime.now()
		));
	}
}
