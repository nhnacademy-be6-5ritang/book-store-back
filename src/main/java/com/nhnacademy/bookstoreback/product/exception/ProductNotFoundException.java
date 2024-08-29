package com.nhnacademy.bookstoreback.product.exception;

import com.nhnacademy.bookstoreback.global.exception.NotFoundException;
import com.nhnacademy.bookstoreback.global.exception.payload.ErrorStatus;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class ProductNotFoundException extends NotFoundException {
    public ProductNotFoundException(Object value) {
        super(ErrorStatus.from(String.format("해당 상품 '%s'는 존재하지 않는 상품 입니다.", value),
                HttpStatus.NOT_FOUND,
                LocalDateTime.now()));
    }
}
