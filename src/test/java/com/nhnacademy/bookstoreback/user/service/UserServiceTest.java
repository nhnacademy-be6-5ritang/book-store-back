package com.nhnacademy.bookstoreback.user.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.nhnacademy.bookstoreback.auth.jwt.dto.CurrentUserDetails;
import com.nhnacademy.bookstoreback.point.transaction.service.PointTransactionService;
import com.nhnacademy.bookstoreback.role.domain.entity.Role;
import com.nhnacademy.bookstoreback.role.repository.RoleRepository;
import com.nhnacademy.bookstoreback.user.domain.dto.request.CreateUserRequest;
import com.nhnacademy.bookstoreback.user.domain.dto.request.UpdateUserInfoRequest;
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
import com.nhnacademy.bookstoreback.userrole.exception.UserHasRoleAlreadyException;
import com.nhnacademy.bookstoreback.userrole.repository.UserRoleRepository;
import com.nhnacademy.bookstoreback.userstatus.domain.entity.UserStatus;
import com.nhnacademy.bookstoreback.userstatus.exception.UserStatusNotFoundException;
import com.nhnacademy.bookstoreback.userstatus.repository.UserStatusRepository;

class UserServiceTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private RoleRepository roleRepository;

	@Mock
	private UserRoleRepository userRoleRepository;

	@Mock
	private UserStatusRepository userStatusRepository;

	@Mock
	private UserGradeRepository userGradeRepository;

	@Mock
	private PointTransactionService pointTransactionService;

	@Mock
	private PasswordEncoder passwordEncoder;

	@InjectMocks
	private UserService userService;

	private User user;
	private UserGrade userGrade;
	private UserStatus userStatus;
	private CurrentUserDetails currentUser;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		userGrade = UserGrade.builder()
			.userGradeName("REGULAR")
			.userGradeMinAmount(BigDecimal.ZERO)
			.userGradeMaxAmount(BigDecimal.valueOf(100000))
			.userGradePointRate(BigDecimal.valueOf(1.5))
			.build();
		userStatus = UserStatus.builder()
			.userStatusName("ACTIVE")
			.build();

		user = User.builder()
			.id(1L)
			.name("Test User")
			.email("test@example.com")
			.password("password")
			.birth(LocalDate.of(1990, 1, 1))
			.contact("01012345678")
			.points(BigDecimal.ZERO)
			.createdAt(LocalDateTime.now())
			.updatedAt(LocalDateTime.now())
			.lastLoginAt(LocalDateTime.now())
			.status(userStatus)
			.userGrade(userGrade)
			.build();

		currentUser = new CurrentUserDetails(
			UserTokenInfo.builder()
				.id(1L)
				.password("password")
				.roles(List.of("ROLE_MEMBER"))
				.status("ACTIVE")
				.build()
		);
	}

	@Test
	void testCreateUser() {
		CreateUserRequest request = CreateUserRequest.builder()
			.name("Test User")
			.email("test@example.com")
			.password("password")
			.year(2000)
			.month(1)
			.day(1)
			.contact("01012345678")
			.build();

		Role memberRole = Role.builder()
			.id(1L)
			.roleName("MEMBER")
			.build();

		when(userRepository.existsByEmail(anyString())).thenReturn(false);
		when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
		when(userRepository.save(any(User.class))).thenReturn(user);
		when(userStatusRepository.findByUserStatusName(anyString())).thenReturn(Optional.of(new UserStatus()));
		when(userGradeRepository.findByUserGradeName(anyString())).thenReturn(Optional.of(new UserGrade()));
		when(roleRepository.findByRoleName("MEMBER")).thenReturn(Optional.of(memberRole));
		when(userRoleRepository.existsByUserAndRole(any(User.class), any(Role.class))).thenReturn(false);

		CreateUserResponse response = userService.createUser(request);

		assertThat(response).isNotNull();
		assertThat(response.email()).isEqualTo(request.email());
	}

	@Test
	void testCreateUserThrowsExceptionWhenEmailExists() {
		CreateUserRequest request = CreateUserRequest.builder()
			.name("Test User")
			.email("existing@example.com")
			.password("password")
			.year(2000)
			.month(1)
			.day(1)
			.contact("01012345678")
			.build();

		when(userRepository.existsByEmail(anyString())).thenReturn(true);

		assertThrows(UserAlreadyExistsException.class, () -> userService.createUser(request));
	}

	@Test
	void testIsEmailExistReturnsTrueWhenEmailExists() {
		when(userRepository.existsByEmail(anyString())).thenReturn(true);

		boolean result = userService.isEmailExist("existing@example.com");

		assertThat(result).isTrue();
	}

	@Test
	void testIsEmailExistReturnsFalseWhenEmailDoesNotExist() {
		when(userRepository.existsByEmail(anyString())).thenReturn(false);

		boolean result = userService.isEmailExist("nonexisting@example.com");

		assertThat(result).isFalse();
	}

	@Test
	void testAddUserRoleByRoleNameThrowsExceptionWhenRoleAlreadyExists() {
		User user = User.builder()
			.id(1L)
			.name("Test User")
			.email("test@example.com")
			.password("encodedPassword")
			.build();

		Role role = Role.builder()
			.id(1L)
			.roleName("MEMBER")
			.build();

		when(roleRepository.findByRoleName(anyString())).thenReturn(Optional.of(role));
		when(userRoleRepository.existsByUserAndRole(any(User.class), any(Role.class))).thenReturn(true);

		assertThrows(UserHasRoleAlreadyException.class, () -> {
			userService.addUserRoleByRoleName(user, "MEMBER");
		});
	}

	@Test
	void testGetMyUserInfoReturnsUserInfo() {
		CurrentUserDetails currentUser = new CurrentUserDetails(UserTokenInfo.builder()
			.id(1L)
			.password("password")
			.roles(List.of("ROLE_MEMBER"))
			.status("ACTIVE")
			.build());

		when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

		GetMyUserInfoResponse response = userService.getMyUserInfo(currentUser);

		assertThat(response).isNotNull();
		assertThat(response.name()).isEqualTo(user.getName());
		assertThat(response.email()).isEqualTo(user.getEmail());
	}

	@Test
	void testGetMyUserInfoThrowsExceptionWhenUserNotFound() {
		CurrentUserDetails currentUser = new CurrentUserDetails(UserTokenInfo.builder()
			.id(1L)
			.password("password")
			.roles(List.of("ROLE_MEMBER"))
			.status("ACTIVE")
			.build());

		when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

		assertThrows(UserNotFoundException.class, () -> {
			userService.getMyUserInfo(currentUser);
		});
	}

	@Test
	void testUpdateUserInfo() {
		UpdateUserInfoRequest request = UpdateUserInfoRequest.builder()
			.name("Updated User")
			.email("updated@example.com")
			.password("newpassword")
			.birth(LocalDate.of(1991, 2, 2))
			.contact("01098765432")
			.build();

		when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
		when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
		when(userRepository.save(any(User.class))).thenReturn(user);

		UpdateUserInfoResponse response = userService.updateUserInfo(currentUser, request);

		assertThat(response).isNotNull();
		assertThat(response.email()).isEqualTo(request.email());
	}

	@Test
	void testWithdrawUser() {
		when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
		when(userStatusRepository.findByUserStatusName(anyString())).thenReturn(Optional.of(new UserStatus()));

		userService.withdrawUser(currentUser);

		verify(userRepository, times(1)).save(user);
	}

	@Test
	void testActivateUser() {
		UserStatus activeStatus = UserStatus.builder()
			.userStatusName("ACTIVE")
			.build();

		when(userStatusRepository.findByUserStatusName("ACTIVE")).thenReturn(Optional.of(activeStatus));

		userService.activateUser(user);

		assertThat(user.getStatus()).isEqualTo(activeStatus);
		verify(userRepository, times(1)).save(user);
	}

	@Test
	void testActivateUserThrowsExceptionWhenStatusNotFound() {
		when(userStatusRepository.findByUserStatusName("ACTIVE")).thenReturn(Optional.empty());

		assertThrows(UserStatusNotFoundException.class, () -> {
			userService.activateUser(user);
		});
	}

	@Test
	void testUpdateLastLoginAt() {
		LocalDateTime now = LocalDateTime.now();

		when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

		userService.updateLastLoginAt(currentUser, now);

		verify(userRepository, times(1)).save(user);
	}

	@Test
	void testGetUserTokenInfoByEmail() {
		when(userRepository.findByEmail(anyString())).thenReturn(user);

		UserTokenInfo response = userService.getUserTokenInfoByEmail("test@example.com");

		assertThat(response).isNotNull();
		assertThat(response.id()).isEqualTo(user.getId());
	}

	@Test
	void testGetUserTokenInfoByPaycoId() {
		when(userRepository.findBySsoId(anyString())).thenReturn(Optional.of(user));

		GetPaycoUserTokenInfoResponse response = userService.getUserTokenInfoByPaycoId("paycoId");

		assertThat(response).isNotNull();
		assertThat(response.id()).isEqualTo(user.getId());
	}

	@Test
	void testGetUserTokenInfoByEmailReturnsUserTokenInfo() {
		String userEmail = "test@example.com";

		when(userRepository.findByEmail(userEmail)).thenReturn(user);

		UserTokenInfo result = userService.getUserTokenInfoByEmail(userEmail);

		assertThat(result).isNotNull();
		assertThat(result.id()).isEqualTo(user.getId());
	}

	@Test
	void testGetUserTokenInfoByEmailReturnsNullWhenUserNotFound() {
		String userEmail = "nonexistent@example.com";

		when(userRepository.findByEmail(userEmail)).thenReturn(null);

		UserTokenInfo result = userService.getUserTokenInfoByEmail(userEmail);

		assertThat(result).isNull();
	}

	@Test
	void testGetUsersWithBirthday() {
		LocalDate date = LocalDate.now();

		when(userRepository.findUsersWithBirthMonthDay(anyInt(), anyInt())).thenReturn(
			List.of(new BirthdayCouponTargetResponse(1L, LocalDate.of(2000, 1, 1))));

		List<BirthdayCouponTargetResponse> responses = userService.getUsersWithBirthday(date);

		assertThat(responses).isNotEmpty();
	}

	@Test
	void testGetUserTokenInfoByPaycoIdThrowsUserNotFoundException() {
		String paycoIdNo = "nonexistentPaycoId";

		when(userRepository.findBySsoId(paycoIdNo)).thenReturn(Optional.empty());

		assertThrows(UserNotFoundException.class, () -> {
			userService.getUserTokenInfoByPaycoId(paycoIdNo);
		});
	}

	@Test
	void testGetUsers() {
		Pageable pageable = PageRequest.of(1, 10, Sort.by(Sort.Direction.DESC, "createdAt"));

		List<User> userList = IntStream.range(0, 10)
			.mapToObj(i -> User.builder()
				.id((long)i)
				.name("User " + i)
				.email("user" + i + "@example.com")
				.createdAt(LocalDateTime.now().minusDays(i))
				.status(userStatus)
				.userGrade(userGrade)
				.build())
			.collect(Collectors.toList());

		PageImpl<User> userPage = new PageImpl<>(userList, pageable, 20);

		when(userRepository.findAll(any(Pageable.class))).thenReturn(userPage);

		Page<GetUserInfoResponse> result = userService.getUsers(pageable);

		assertThat(result).isNotNull();
		assertThat(result.getContent().size()).isEqualTo(10);
		assertThat(result.getContent().get(0).name()).isEqualTo("User 0");
		assertThat(result.getContent().get(0).email()).isEqualTo("user0@example.com");
	}
}
