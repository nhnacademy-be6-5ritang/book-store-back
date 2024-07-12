package com.nhnacademy.bookstoreback.user.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nhnacademy.bookstoreback.auth.annotation.CurrentUser;
import com.nhnacademy.bookstoreback.auth.jwt.dto.CurrentUserDetails;
import com.nhnacademy.bookstoreback.order.service.OrderService;
import com.nhnacademy.bookstoreback.user.domain.dto.request.CreateUserRequest;
import com.nhnacademy.bookstoreback.user.domain.dto.request.UpdateUserInfoRequest;
import com.nhnacademy.bookstoreback.user.domain.dto.response.BirthdayCouponTargetResponse;
import com.nhnacademy.bookstoreback.user.domain.dto.response.CreateUserResponse;
import com.nhnacademy.bookstoreback.user.domain.dto.response.GetMyUserInfoResponse;
import com.nhnacademy.bookstoreback.user.domain.dto.response.UpdateUserInfoResponse;
import com.nhnacademy.bookstoreback.user.service.MailService;
import com.nhnacademy.bookstoreback.user.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
	private final UserService userService;
	private final MailService mailService;
	private final OrderService orderService;

	@PostMapping
	public ResponseEntity<CreateUserResponse> signUpUser(@RequestBody CreateUserRequest createUserRequest) {
		CreateUserResponse createUserResponse = userService.createUser(createUserRequest);
		return ResponseEntity.status(HttpStatus.CREATED).body(createUserResponse);
	}

	@PostMapping("/send-email/sign-up")
	public ResponseEntity<Void> sendMailSignUp(@RequestParam String email) {
		String subject = "회원가입";
		boolean isEmailExist = userService.isEmailExist(email);
		if (isEmailExist) {
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		}
		mailService.sendMail(email, subject);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@PostMapping("/send-email/dormant-to-active")
	public ResponseEntity<Void> sendMailDormantToActive(@RequestParam String email) {
		String subject = "휴면계정 활성화";
		mailService.sendMail(email, subject);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@GetMapping("/check-email/sign-up")
	public ResponseEntity<Void> checkMailSignUp(@RequestParam String email, @RequestParam String certifyCode) {
		String subject = "회원가입";
		boolean codeMatch = mailService.checkMail(email, certifyCode, subject);
		if (codeMatch) {
			return ResponseEntity.status(HttpStatus.OK).build();
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}

	@GetMapping("/check-email/dormant-to-active")
	public ResponseEntity<Boolean> checkMailDormantToActive(@RequestParam String email,
		@RequestParam String certifyCode) {
		String subject = "휴면계정 활성화";
		boolean codeMatch = mailService.checkMail(email, certifyCode, subject);
		return ResponseEntity.status(HttpStatus.OK).body(codeMatch);
	}

	@GetMapping("/self")
	public ResponseEntity<GetMyUserInfoResponse> getMyUserInfo(@CurrentUser CurrentUserDetails currentUser) {
		GetMyUserInfoResponse getMyUserInfoResponse = userService.getMyUserInfo(currentUser);
		return ResponseEntity.status(HttpStatus.OK).body(getMyUserInfoResponse);
	}

	@PutMapping
	public ResponseEntity<UpdateUserInfoResponse> updateUser(
		@CurrentUser CurrentUserDetails currentUser, @RequestBody UpdateUserInfoRequest updateUserInfoRequest
	) {
		UpdateUserInfoResponse updateUserInfoResponse = userService.updateUserInfo(currentUser, updateUserInfoRequest);
		return ResponseEntity.status(HttpStatus.OK).body(updateUserInfoResponse);
	}

	@PatchMapping("/withdraw")
	public ResponseEntity<Void> withdrawUser(@CurrentUser CurrentUserDetails currentUser) {
		userService.withdrawUser(currentUser);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@GetMapping("/self/total-order-price")
	public ResponseEntity<BigDecimal> getTotalOrderPrice(@CurrentUser CurrentUserDetails currentUser) {
		BigDecimal totalPaymentAmount = orderService.getTotalOrderPrice(currentUser);
		return ResponseEntity.status(HttpStatus.OK).body(totalPaymentAmount);
	}

	@PatchMapping("/last-login-at")
	public ResponseEntity<Void> updateLastLoginAt(
		@CurrentUser CurrentUserDetails currentUser, @RequestBody LocalDateTime lastLoginAt
	) {
		userService.updateLastLoginAt(currentUser, lastLoginAt);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	/**
	 * @author 이기훈
	 * @param date 유저 생일
	 * @return 해당 날짜가 생일인 유저의 생일리스트를 리턴
	 */
	@GetMapping("/birthday")
	public ResponseEntity<List<BirthdayCouponTargetResponse>> getUsersWithBirthday(
		@RequestParam("date") LocalDate date) {
		log.warn("{} 생일쿠폰 발급 컨트롤러 실행", date);
		List<BirthdayCouponTargetResponse> users = userService.getUsersWithBirthday(date);
		return ResponseEntity.ok(users);
	}

}
