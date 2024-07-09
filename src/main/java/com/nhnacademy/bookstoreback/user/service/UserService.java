package com.nhnacademy.bookstoreback.user.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nhnacademy.bookstoreback.auth.annotation.CurrentUser;
import com.nhnacademy.bookstoreback.auth.jwt.dto.CurrentUserDetails;
import com.nhnacademy.bookstoreback.point.transaction.service.PointTransactionService;
import com.nhnacademy.bookstoreback.role.domain.entity.Role;
import com.nhnacademy.bookstoreback.role.exception.RoleNotFoundException;
import com.nhnacademy.bookstoreback.role.repository.RoleRepository;
import com.nhnacademy.bookstoreback.user.domain.dto.request.CreateUserRequest;
import com.nhnacademy.bookstoreback.user.domain.dto.request.UpdateUserInfoRequest;
import com.nhnacademy.bookstoreback.user.domain.dto.response.BirthdayCouponTargetResponse;
import com.nhnacademy.bookstoreback.user.domain.dto.response.CreateUserResponse;
import com.nhnacademy.bookstoreback.user.domain.dto.response.GetMyUserInfoResponse;
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

@Service
@RequiredArgsConstructor
@Transactional
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

	public boolean isEmailExist(String email) {
		return userRepository.existsByEmail(email);
	}

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

	public GetMyUserInfoResponse getMyUserInfo(@CurrentUser CurrentUserDetails currentUser) {
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
		User user = userRepository.findById(updateUserInfoRequest.id())
			.orElseThrow(() -> new UserNotFoundException(updateUserInfoRequest.id()));

		// if (Objects.isNull(currentUser) || !user.getId().equals(currentUser.getUserId())) {
		// 	throw new AccessDeniedException(currentUser.getUserId(), user.getId());
		// }

		UpdateUserInfoRequest updateUserInfoRequestWithEncodedPassword
			= updateUserInfoRequest.encodePassword(passwordEncoder);
		user.update(updateUserInfoRequestWithEncodedPassword);
		User updatedUser = userRepository.save(user);

		return UpdateUserInfoResponse.fromEntity(updatedUser);
	}

	public void dormantUser(@CurrentUser CurrentUserDetails currentUser) {
		User user = userRepository.findById(currentUser.getUserId())
			.orElseThrow(() -> new UserNotFoundException(currentUser.getUserId()));

		UserStatus dormantUserStatus = userStatusRepository.findByUserStatusName("DORMANT")
			.orElseThrow(() -> new UserStatusNotFoundException("DORMANT"));
		user.updateUserStatus(dormantUserStatus);

		userRepository.save(user);
	}

	public UserTokenInfo getUserTokenInfoByEmail(String userEmail) {
		User user = userRepository.findByEmail(userEmail);

		if (Objects.isNull(user)) {
			return null;
		}

		return UserTokenInfo.fromEntity(user);
	}

	/**
	 *  @author 이기훈
	 *
	 * 생일쿠폰 발급시 생일 정보를 얻기위한 service
	 *
	 */
	@Transactional(readOnly = true)
	public List<BirthdayCouponTargetResponse> getUsersWithBirthday(LocalDate date) {
		return userRepository.findByBirthDate(date);
	}
}
