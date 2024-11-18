package com.nhnacademy.bookstoreback.user.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nhnacademy.bookstoreback.auth.annotation.CurrentUser;
import com.nhnacademy.bookstoreback.auth.jwt.dto.CurrentUserDetails;
import com.nhnacademy.bookstoreback.global.exception.OrderFailException;
import com.nhnacademy.bookstoreback.global.exception.payload.ErrorStatus;
import com.nhnacademy.bookstoreback.point.transaction.service.PointTransactionService;
import com.nhnacademy.bookstoreback.role.domain.entity.Role;
import com.nhnacademy.bookstoreback.role.exception.RoleNotFoundException;
import com.nhnacademy.bookstoreback.role.repository.RoleRepository;
import com.nhnacademy.bookstoreback.user.domain.dto.request.CreateUserRequest;
import com.nhnacademy.bookstoreback.user.domain.dto.request.UpdateUserInfoRequest;
import com.nhnacademy.bookstoreback.user.domain.dto.request.UpdateUserRoleRequest;
import com.nhnacademy.bookstoreback.user.domain.dto.response.BirthdayCouponTargetResponse;
import com.nhnacademy.bookstoreback.user.domain.dto.response.CreateUserResponse;
import com.nhnacademy.bookstoreback.user.domain.dto.response.GetMyUserInfoResponse;
import com.nhnacademy.bookstoreback.user.domain.dto.response.GetPaycoUserTokenInfoResponse;
import com.nhnacademy.bookstoreback.user.domain.dto.response.GetUserInfoResponse;
import com.nhnacademy.bookstoreback.user.domain.dto.response.UpdateUserInfoResponse;
import com.nhnacademy.bookstoreback.user.domain.dto.response.UserTokenInfo;
import com.nhnacademy.bookstoreback.user.domain.entity.User;
import com.nhnacademy.bookstoreback.user.exception.UserAlreadyExistsException;
import com.nhnacademy.bookstoreback.user.exception.UserNotFoundException;
import com.nhnacademy.bookstoreback.user.repository.UserRepository;
import com.nhnacademy.bookstoreback.usergrade.domain.entity.UserGrade;
import com.nhnacademy.bookstoreback.usergrade.repository.UserGradeRepository;
import com.nhnacademy.bookstoreback.userrole.domain.entity.UserRole;
import com.nhnacademy.bookstoreback.userrole.exception.UserHasRoleAlreadyException;
import com.nhnacademy.bookstoreback.userrole.repository.UserRoleRepository;
import com.nhnacademy.bookstoreback.userstatus.domain.entity.UserStatus;
import com.nhnacademy.bookstoreback.userstatus.exception.UserStatusNotFoundException;
import com.nhnacademy.bookstoreback.userstatus.repository.UserStatusRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 김태환
 * 사용자 서비스 클래스.
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService {
	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final UserRoleRepository userRoleRepository;
	private final UserStatusRepository userStatusRepository;
	private final UserGradeRepository userGradeRepository;

	private final PointTransactionService pointTransactionService;

	private final PasswordEncoder passwordEncoder;

	/**
	 * 회원가입; 사용자 정보를 생성합니다.
	 *
	 * @param createUserRequest 사용자 정보: [이름, 이메일, 비밀번호, 생년월일, 연락처]
	 * @return 생성된 사용자 정보 응답
	 */
	public CreateUserResponse createUser(CreateUserRequest createUserRequest) {
		if (userRepository.existsByEmail(createUserRequest.email())) {
			throw new UserAlreadyExistsException(createUserRequest.email());
		}

		String encodedPassword = passwordEncoder.encode(createUserRequest.password());
		User user = User.toEntity(createUserRequest, encodedPassword);

		User savedUser = userRepository.save(user);

		addUserRoleByRoleName(savedUser, "MEMBER");

		UserStatus defaultUserStatus = userStatusRepository.findByUserStatusName("ACTIVE")
			.orElseThrow(() -> new UserStatusNotFoundException("ACTIVE"));
		savedUser.updateUserStatus(defaultUserStatus);

		UserGrade defaultUserGrade = userGradeRepository.findByUserGradeName("REGULAR")
			.orElseThrow(() -> new UserStatusNotFoundException("REGULAR"));
		savedUser.updateUserGrade(defaultUserGrade);

		pointTransactionService.signUpPointTransaction(savedUser);

		savedUser = userRepository.save(savedUser);

		return CreateUserResponse.fromEntity(savedUser);
	}

	/**
	 * 이메일이 존재하는지 확인합니다.
	 *
	 * @param email 확인할 이메일 주소
	 * @return 존재하면 true, 그렇지 않으면 false
	 */
	public boolean isEmailExist(String email) {
		return userRepository.existsByEmail(email);
	}

	/**
	 * 사용자에게 역할을 추가합니다.
	 *
	 * @param user 사용자 엔티티
	 * @param roleName 추가할 역할 이름
	 */
	public void addUserRoleByRoleName(User user, String roleName) {
		Role role = roleRepository.findByRoleName(roleName)
			.orElseThrow(() -> new RoleNotFoundException(roleName));

		UserRole userRole = UserRole.builder()
			.user(user)
			.role(role)
			.build();

		if (userRoleRepository.existsByUserAndRole(user, role)) {
			throw new UserHasRoleAlreadyException(user.getId(), role.getRoleName());
		}

		userRoleRepository.save(userRole);
	}

	/**
	 * 현재 사용자의 정보를 조회합니다.
	 *
	 * @param currentUser 현재 사용자 세부 정보
	 * @return 현재 사용자의 정보 응답 DTO
	 */
	public GetMyUserInfoResponse getMyUserInfo(@CurrentUser CurrentUserDetails currentUser) {
		User user = userRepository.findById(currentUser.getUserId())
			.orElseThrow(() -> new UserNotFoundException(currentUser.getUserId()));

		return GetMyUserInfoResponse.fromEntity(user);
	}

	/**
	 * 주문에 따라 현재 사용자의 정보를 조회합니다.
	 *
	 * @param currentUser 현재 사용자 세부 정보
	 * @return 현재 사용자의 정보 응답 DTO 또는 null
	 */
	public GetMyUserInfoResponse getMyUserInfoByOrder(@CurrentUser CurrentUserDetails currentUser) {
		if (currentUser == null) {
			return null;
		}

		User user = userRepository.findById(currentUser.getUserId())
			.orElseThrow(() -> new UserNotFoundException(currentUser.getUserId()));
		return GetMyUserInfoResponse.fromEntity(user);
	}

	/**
	 * 사용자 정보를 수정합니다.
	 *
	 * @param currentUser 현재 요청한 사용자 정보
	 * @param updateUserInfoRequest 사용자 ID, 수정될 것: [이름, 이메일, 비밀번호, 생년월일, 연락처]
	 * @return 수정된 사용자 정보 응답
	 */
	public UpdateUserInfoResponse updateUserInfo(
		@CurrentUser CurrentUserDetails currentUser,
		UpdateUserInfoRequest updateUserInfoRequest
	) {
		if (currentUser == null) {
			ErrorStatus errorStatus = ErrorStatus.from("유저를 찾을 수 없습니다.", HttpStatus.NOT_FOUND, LocalDateTime.now());
			throw new OrderFailException(errorStatus);
		}
		User user = userRepository.findById(currentUser.getUserId())
			.orElseThrow(() -> new UserNotFoundException(currentUser.getUserId()));

		if (updateUserInfoRequest.password() == null || updateUserInfoRequest.password().isEmpty()) {
			user.updateNotPassword(updateUserInfoRequest);
			User updatedUser = userRepository.save(user);
			return UpdateUserInfoResponse.fromEntity(updatedUser);
		}
		UpdateUserInfoRequest updateUserInfoRequestWithEncodedPassword
			= updateUserInfoRequest.encodePassword(passwordEncoder);
		user.update(updateUserInfoRequestWithEncodedPassword);
		User updatedUser = userRepository.save(user);

		return UpdateUserInfoResponse.fromEntity(updatedUser);
	}

	/**
	 * 사용자를 탈퇴 처리합니다.
	 *
	 * @param currentUser 현재 사용자 세부 정보
	 */
	public void withdrawUser(@CurrentUser CurrentUserDetails currentUser) {
		User user = userRepository.findById(currentUser.getUserId())
			.orElseThrow(() -> new UserNotFoundException(currentUser.getUserId()));

		UserStatus withdrawUserStatus = userStatusRepository.findByUserStatusName("WITHDRAW")
			.orElseThrow(() -> new UserStatusNotFoundException("WITHDRAW"));
		user.updateUserStatus(withdrawUserStatus);

		userRepository.save(user);
	}

	/**
	 * 사용자를 활성화합니다.
	 *
	 * @param user 활성화할 사용자 엔티티
	 */
	public void activateUser(User user) {
		user.updateUserStatus(userStatusRepository.findByUserStatusName("ACTIVE")
			.orElseThrow(() -> new UserStatusNotFoundException("ACTIVE")));

		userRepository.save(user);
	}

	/**
	 * 이메일을 통해 사용자 토큰 정보를 조회합니다.
	 *
	 * @param userEmail 사용자 이메일
	 * @return 사용자 토큰 정보 응답 DTO
	 */
	public UserTokenInfo getUserTokenInfoByEmail(String userEmail) {
		User user = userRepository.findByEmail(userEmail);

		if (Objects.isNull(user)) {
			return null;
		}

		return UserTokenInfo.fromEntity(user);
	}

	/**
	 * @author 이기훈
	 * 생일 쿠폰 발급 대상자를 조회합니다.
	 *
	 * @param date 조회할 날짜
	 * @return 생일 쿠폰 발급 대상자 목록 응답 DTO
	 */
	public List<BirthdayCouponTargetResponse> getUsersWithBirthday(LocalDate date) {
		int month = date.getMonthValue();
		int day = date.getDayOfMonth();
		log.warn("{}, 월 {} , 일 {} 생일쿠폰 발급 서비스 실행", date, month, day);

		return userRepository.findUsersWithBirthMonthDay(month, day);
	}

	/**
	 * 사용자의 마지막 로그인 시간을 업데이트합니다.
	 *
	 * @param currentUser 현재 사용자 세부 정보
	 * @param lastLoginAt 업데이트할 마지막 로그인 시간
	 */
	public void updateLastLoginAt(CurrentUserDetails currentUser, LocalDateTime lastLoginAt) {
		User user = userRepository.findById(currentUser.getUserId())
			.orElseThrow(() -> new UserNotFoundException(currentUser.getUserId()));

		user.updateLastLoginAt(lastLoginAt);
		userRepository.save(user);
	}

	/**
	 * PAYCO ID를 통해 사용자 토큰 정보를 조회합니다.
	 *
	 * @param paycoIdNo PAYCO ID
	 * @return PAYCO 사용자 토큰 정보 응답 DTO
	 */
	public GetPaycoUserTokenInfoResponse getUserTokenInfoByPaycoId(String paycoIdNo) {
		User user = userRepository.findBySsoId(paycoIdNo).orElseThrow(
			() -> new UserNotFoundException(paycoIdNo)
		);

		return GetPaycoUserTokenInfoResponse.fromEntity(user);
	}

	/**
	 * 페이지네이션된 사용자 정보를 조회합니다.
	 *
	 * @param pageable 페이지 정보
	 * @return 페이지네이션된 사용자 정보 응답 DTO 목록
	 */
	public Page<GetUserInfoResponse> getUsers(Pageable pageable) {
		int page = pageable.getPageNumber() > 0 ? pageable.getPageNumber() - 1 : 0;
		int size = pageable.isPaged() && pageable.getPageSize() > 0 ? pageable.getPageSize() : 10;

		return userRepository.findAll(PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt")))
			.map(GetUserInfoResponse::fromEntity);
	}

	/**
	 * 사용자 역할을 업데이트합니다.
	 *
	 * @param updateUserRoleRequest 사용자 역할 업데이트 요청 DTO
	 */
	public void updateUserRoleByRoleName(UpdateUserRoleRequest updateUserRoleRequest) {
		User user = userRepository.findById(updateUserRoleRequest.userId()).orElse(null);

		List<UserRole> userRoles = userRoleRepository.findByUser(user);
		userRoleRepository.deleteAll(userRoles);

		for (String roleName : updateUserRoleRequest.roleName()) {
			Role role = roleRepository.findByRoleName(roleName)
				.orElseThrow(() -> new RoleNotFoundException(roleName));
			UserRole userRole = UserRole.builder()
				.user(user)
				.role(role)
				.build();
			userRoleRepository.save(userRole);
		}
	}

	// TODO: currentUser 대신 user id로 변경
	public void paycoConnect(CurrentUserDetails currentUser, String memberId) {
		User user = userRepository.findById(currentUser.getUserId())
			.orElseThrow(() -> new UserNotFoundException(currentUser.getUserId()));

		user.updateSsoId(memberId);
		userRepository.save(user);
	}
}
