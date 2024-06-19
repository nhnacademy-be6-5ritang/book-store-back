package com.nhnacademy.bookstoreback.wishlist.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nhnacademy.bookstoreback.wishlist.domain.entity.WishList;

public interface WishListRepository extends JpaRepository<WishList, Long> {
	List<WishList> findAllByUserId(Long userId);
}
