package com.nhnacademy.bookstoreback.user.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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

import com.nhnacademy.bookstoreback.auth.annotation.AuthorizeRole;
import com.nhnacademy.bookstoreback.auth.annotation.CurrentUser;
import com.nhnacademy.bookstoreback.auth.jwt.dto.CurrentUserDetails;
import com.nhnacademy.bookstoreback.order.service.OrderService;
import com.nhnacademy.bookstoreback.user.domain.dto.request.CreateUserRequest;
import com.nhnacademy.bookstoreback.user.domain.dto.request.UpdateUserInfoRequest;
import com.nhnacademy.bookstoreback.user.domain.dto.request.UpdateUserRoleRequest;
import com.nhnacademy.bookstoreback.user.domain.dto.response.BirthdayCouponTargetResponse;
import com.nhnacademy.bookstoreback.user.domain.dto.response.CreateUserResponse;
import com.nhnacademy.bookstoreback.user.domain.dto.response.GetMyUserInfoResponse;
import com.nhnacademy.bookstoreback.user.domain.dto.response.GetUserInfoResponse;
import com.nhnacademy.bookstoreback.user.domain.dto.response.UpdateUserInfoResponse;
import com.nhnacademy.bookstoreback.user.domain.entity.User;
import com.nhnacademy.bookstoreback.user.repository.UserRepository;
import com.nhnacademy.bookstoreback.user.service.MailService;
import com.nhnacademy.bookstoreback.user.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 김태환
 * 사용자 관련 HTTP 요청을 처리하는 컨트롤러입니다.
 */
@Tag(name = "User", description = "사용자 관련 API")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
	private final UserService userService;
	private final MailService mailService;
	private final OrderService orderService;
	private final UserRepository userRepository;

	/**
	 * 관리자 페이지 접근을 위한 엔드포인트입니다.
	 *
	 * @return 성공적으로 접근했음을 나타내는 HTTP 200 상태 코드.
	 */
	@Operation(
		summary = "관리자 페이지 접근",
		description = "특정 권한을 가진 사용자만 접근할 수 있는 관리자 페이지 엔드포인트입니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "성공적으로 접근되었습니다.")
	})
	@GetMapping("/admin-page")
	@AuthorizeRole({"MEMBER_ADMIN", "BOOK_ADMIN", "ORDER_ADMIN", "COUPON_ADMIN", "DELIVERY_ADMIN", "HEAD_ADMIN"})
	public ResponseEntity<Void> adminPage() {
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	/**
	 * 새로운 사용자를 생성합니다.
	 *
	 * @param createUserRequest 사용자 생성에 필요한 정보.
	 * @return 생성된 사용자 정보를 포함하는 HTTP 201 상태 코드 응답.
	 */
	@Operation(
		summary = "사용자 생성",
		description = "새로운 사용자를 생성합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "사용자가 성공적으로 생성되었습니다."),
		@ApiResponse(responseCode = "400", description = "잘못된 요청 데이터입니다."),
		@ApiResponse(responseCode = "409", description = "이미 존재하는 사용자입니다.")
	})
	@PostMapping
	public ResponseEntity<CreateUserResponse> signUpUser(@Valid @RequestBody CreateUserRequest createUserRequest) {
		CreateUserResponse createUserResponse = userService.createUser(createUserRequest);
		return ResponseEntity.status(HttpStatus.CREATED).body(createUserResponse);
	}

	/**
	 * 회원가입을 위한 이메일을 전송합니다.
	 *
	 * @param email 사용자 이메일.
	 * @return 이메일이 성공적으로 전송되었음을 나타내는 HTTP 200 상태 코드 응답.
	 */
	@Operation(
		summary = "회원가입 이메일 전송",
		description = "회원가입을 위한 이메일을 전송합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "이메일이 성공적으로 전송되었습니다."),
		@ApiResponse(responseCode = "409", description = "이미 존재하는 이메일입니다.")
	})
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

	/**
	 * 휴면 계정 활성화를 위한 이메일을 전송합니다.
	 *
	 * @param email 사용자 이메일.
	 * @return 이메일이 성공적으로 전송되었음을 나타내는 HTTP 200 상태 코드 응답.
	 */
	@Operation(
		summary = "휴면 계정 활성화 이메일 전송",
		description = "휴면 계정을 활성화하기 위한 이메일을 전송합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "이메일이 성공적으로 전송되었습니다.")
	})
	@PostMapping("/send-email/dormant-to-active")
	public ResponseEntity<Void> sendMailDormantToActive(@RequestParam String email) {
		String subject = "휴면계정 활성화";
		mailService.sendMail(email, subject);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	/**
	 * 회원가입 이메일 인증을 확인합니다.
	 *
	 * @param email 사용자 이메일.
	 * @param certifyCode 인증 코드.
	 * @return 인증 코드가 일치하면 HTTP 200 상태 코드, 일치하지 않으면 HTTP 401 상태 코드 응답.
	 */
	@Operation(
		summary = "회원가입 이메일 인증",
		description = "회원가입 이메일 인증 코드를 확인합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "이메일 인증 코드가 일치합니다."),
		@ApiResponse(responseCode = "401", description = "이메일 인증 코드가 일치하지 않습니다.")
	})
	@GetMapping("/check-email/sign-up")
	public ResponseEntity<Void> checkMailSignUp(@RequestParam String email, @RequestParam String certifyCode) {
		String subject = "회원가입";
		boolean codeMatch = mailService.checkMail(email, certifyCode, subject);
		if (codeMatch) {
			return ResponseEntity.status(HttpStatus.OK).build();
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}

	/**
	 * 휴면 계정 활성화를 위한 이메일 인증을 확인합니다.
	 *
	 * @param email 사용자 이메일.
	 * @param certifyCode 인증 코드.
	 * @return 인증 코드가 일치하면 HTTP 200 상태 코드와 함께 활성화된 사용자 정보, 일치하지 않으면 HTTP 401 상태 코드 응답.
	 */
	@Operation(
		summary = "휴면 계정 활성화 이메일 인증",
		description = "휴면 계정 활성화 이메일 인증 코드를 확인합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "이메일 인증 코드가 일치합니다."),
		@ApiResponse(responseCode = "401", description = "이메일 인증 코드가 일치하지 않습니다.")
	})
	@GetMapping("/check-email/dormant-to-active")
	public ResponseEntity<Void> checkMailDormantToActive(@RequestParam String email,
		@RequestParam String certifyCode) {
		String subject = "휴면계정 활성화";
		boolean codeMatch = mailService.checkMail(email, certifyCode, subject);
		if (codeMatch) {
			User user = userRepository.findByEmail(email);
			userService.activateUser(user);
			return ResponseEntity.status(HttpStatus.OK).build();
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}

	/**
	 * 모든 사용자 정보를 페이징하여 조회합니다.
	 *
	 * @param pageable 페이지 정보 (페이지 번호와 페이지 크기)
	 * @return 사용자 정보 목록의 페이징 결과
	 */
	@Operation(
		summary = "모든 사용자 정보 페이징 조회",
		description = "모든 사용자 정보를 페이징하여 조회합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "사용자 정보 페이징 조회 성공")
	})
	@GetMapping
	@AuthorizeRole({"MEMBER_ADMIN", "HEAD_ADMIN"})
	public ResponseEntity<Page<GetUserInfoResponse>> getUsers(@PageableDefault(size = 10) Pageable pageable) {
		Page<GetUserInfoResponse> getUserInfoResponses = userService.getUsers(pageable);
		return ResponseEntity.status(HttpStatus.OK).body(getUserInfoResponses);
	}

	/**
	 * 현재 사용자의 정보를 조회합니다.
	 *
	 * @param currentUser 현재 로그인한 사용자 정보
	 * @return 현재 사용자의 정보
	 */
	@Operation(
		summary = "현재 사용자 정보 조회",
		description = "현재 로그인한 사용자의 정보를 조회합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "현재 사용자 정보 조회 성공")
	})
	@GetMapping("/self")
	@AuthorizeRole({"MEMBER", "MEMBER_ADMIN", "HEAD_ADMIN"})
	public ResponseEntity<GetMyUserInfoResponse> getMyUserInfo(@CurrentUser CurrentUserDetails currentUser) {
		GetMyUserInfoResponse getMyUserInfoResponse = userService.getMyUserInfo(currentUser);
		return ResponseEntity.status(HttpStatus.OK).body(getMyUserInfoResponse);
	}

	/**
	 * 현재 사용자의 정보를 주문 정보를 통해 조회합니다.
	 *
	 * @param currentUser 현재 로그인한 사용자 정보
	 * @return 현재 사용자의 주문 정보를 기반으로 한 사용자 정보
	 */
	@Operation(
		summary = "주문 정보를 기반으로 현재 사용자 정보 조회",
		description = "현재 사용자의 주문 정보를 기반으로 사용자 정보를 조회합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "주문 정보를 기반으로 한 사용자 정보 조회 성공")
	})
	@GetMapping("/orders/self")
	public ResponseEntity<GetMyUserInfoResponse> getMyUserInfoByInfo(@CurrentUser CurrentUserDetails currentUser) {
		GetMyUserInfoResponse getMyUserInfoResponse = userService.getMyUserInfoByOrder(currentUser);
		return ResponseEntity.status(HttpStatus.OK).body(getMyUserInfoResponse);
	}

	/**
	 * 현재 사용자의 정보를 업데이트합니다.
	 *
	 * @param currentUser 현재 로그인한 사용자 정보
	 * @param updateUserInfoRequest 업데이트할 사용자 정보 DTO
	 * @return 업데이트된 사용자 정보
	 */
	@Operation(
		summary = "현재 사용자 정보 업데이트",
		description = "현재 사용자의 정보를 업데이트합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "사용자 정보 업데이트 성공"),
		@ApiResponse(responseCode = "400", description = "잘못된 요청 데이터입니다.")
	})
	@PutMapping
	@AuthorizeRole({"MEMBER", "MEMBER_ADMIN", "HEAD_ADMIN"})
	public ResponseEntity<UpdateUserInfoResponse> updateUser(
		@CurrentUser CurrentUserDetails currentUser, @Valid @RequestBody UpdateUserInfoRequest updateUserInfoRequest
	) {
		UpdateUserInfoResponse updateUserInfoResponse = userService.updateUserInfo(currentUser, updateUserInfoRequest);
		return ResponseEntity.status(HttpStatus.OK).body(updateUserInfoResponse);
	}

	/**
	 * 현재 사용자의 계정을 탈퇴 처리합니다.
	 *
	 * @param currentUser 현재 로그인한 사용자 정보
	 * @return 응답 상태 코드 (204 No Content)
	 */
	@Operation(
		summary = "현재 사용자 계정 탈퇴",
		description = "현재 사용자의 계정을 탈퇴 처리합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "204", description = "사용자 계정 탈퇴 성공")
	})
	@PatchMapping("/withdraw")
	@AuthorizeRole({"MEMBER", "MEMBER_ADMIN", "HEAD_ADMIN"})
	public ResponseEntity<Void> withdrawUser(@CurrentUser CurrentUserDetails currentUser) {
		userService.withdrawUser(currentUser);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	/**
	 * 현재 사용자의 총 주문 금액을 조회합니다.
	 *
	 * @param currentUser 현재 로그인한 사용자 정보
	 * @return 총 주문 금액
	 */
	@Operation(
		summary = "현재 사용자의 총 주문 금액 조회",
		description = "현재 사용자의 총 주문 금액을 조회합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "총 주문 금액 조회 성공")
	})
	@GetMapping("/self/total-order-price")
	@AuthorizeRole({"MEMBER", "MEMBER_ADMIN", "HEAD_ADMIN"})
	public ResponseEntity<BigDecimal> getTotalOrderPrice(@CurrentUser CurrentUserDetails currentUser) {
		BigDecimal totalPaymentAmount = orderService.getTotalOrderPrice(currentUser);

		return ResponseEntity.status(HttpStatus.OK).body(totalPaymentAmount);
	}

	/**
	 * 현재 사용자의 마지막 로그인 시간을 업데이트합니다.
	 *
	 * @param currentUser 현재 로그인한 사용자 정보
	 * @param lastLoginAt 업데이트할 마지막 로그인 시간
	 * @return 응답 상태 코드 (200 OK)
	 */
	@Operation(
		summary = "현재 사용자의 마지막 로그인 시간 업데이트",
		description = "현재 사용자의 마지막 로그인 시간을 업데이트합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "마지막 로그인 시간 업데이트 성공")
	})
	@PatchMapping("/last-login-at")
	public ResponseEntity<Void> updateLastLoginAt(
		@CurrentUser CurrentUserDetails currentUser, @Valid @RequestBody LocalDateTime lastLoginAt
	) {
		userService.updateLastLoginAt(currentUser, lastLoginAt);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	/**
	 * @author 이기훈
	 * @param date 유저 생일
	 * @return 해당 날짜가 생일인 유저의 생일리스트를 리턴
	 */
	@Operation(
		summary = "생일이 같은 사용자 조회",
		description = "특정 날짜에 생일인 사용자 목록을 조회합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "생일이 같은 사용자 목록 조회 성공")
	})
	@GetMapping("/birthday")
	public ResponseEntity<List<BirthdayCouponTargetResponse>> getUsersWithBirthday(
		@RequestParam("date") LocalDate date) {
		log.warn("{} 생일쿠폰 발급 컨트롤러 실행", date);
		List<BirthdayCouponTargetResponse> users = userService.getUsersWithBirthday(date);
		return ResponseEntity.ok(users);
	}

	/**
	 * 사용자의 역할을 업데이트합니다.
	 *
	 * @param updateUserRoleRequest 업데이트할 사용자 역할 정보 DTO
	 * @return 응답 상태 코드 (204 No Content)
	 */
	@Operation(
		summary = "사용자 역할 업데이트",
		description = "사용자의 역할을 업데이트합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "204", description = "사용자 역할 업데이트 성공")
	})
	@PutMapping("/role")
	public ResponseEntity<Void> updateRole(@RequestBody UpdateUserRoleRequest updateUserRoleRequest) {
		userService.updateUserRoleByRoleName(updateUserRoleRequest);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@GetMapping("/payco-connect/{memberId}")
	public ResponseEntity<Void> paycoConnect(@CurrentUser CurrentUserDetails currentUser,
		@RequestParam String memberId) {
		userService.paycoConnect(currentUser, memberId);
		return ResponseEntity.status(HttpStatus.OK).build();
	}
}
