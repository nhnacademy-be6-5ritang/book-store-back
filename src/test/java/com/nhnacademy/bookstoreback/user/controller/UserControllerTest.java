package com.nhnacademy.bookstoreback.user.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookstoreback.auth.jwt.dto.CurrentUserDetails;
import com.nhnacademy.bookstoreback.order.service.OrderService;
import com.nhnacademy.bookstoreback.user.domain.dto.request.CreateUserRequest;
import com.nhnacademy.bookstoreback.user.domain.dto.request.UpdateUserInfoRequest;
import com.nhnacademy.bookstoreback.user.domain.dto.response.BirthdayCouponTargetResponse;
import com.nhnacademy.bookstoreback.user.domain.dto.response.CreateUserResponse;
import com.nhnacademy.bookstoreback.user.domain.dto.response.GetMyUserInfoResponse;
import com.nhnacademy.bookstoreback.user.domain.dto.response.GetUserInfoResponse;
import com.nhnacademy.bookstoreback.user.domain.dto.response.UpdateUserInfoResponse;
import com.nhnacademy.bookstoreback.user.domain.dto.response.UserTokenInfo;
import com.nhnacademy.bookstoreback.user.domain.entity.User;
import com.nhnacademy.bookstoreback.user.repository.UserRepository;
import com.nhnacademy.bookstoreback.user.service.MailService;
import com.nhnacademy.bookstoreback.user.service.UserService;
import com.nhnacademy.bookstoreback.usergrade.domain.entity.UserGrade;
import com.nhnacademy.bookstoreback.userstatus.domain.entity.UserStatus;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserService userService;

	@MockBean
	private MailService mailService;

	@MockBean
	private OrderService orderService;

	@MockBean
	private UserRepository userRepository;

	@MockBean
	private PasswordEncoder passwordEncoder;

	@Autowired
	private ObjectMapper objectMapper;

	private CurrentUserDetails currentUser;

	private User user;

	@BeforeEach
	void setUp() {
		UserTokenInfo userTokenInfo = UserTokenInfo.builder()
			.id(1L)
			.password("password")
			.roles(List.of("ROLE_MEMBER"))
			.status("ACTIVE")
			.build();

		currentUser = new CurrentUserDetails(userTokenInfo);

		UserGrade userGrade = UserGrade.builder()
			.userGradeName("REGULAR")
			.userGradeMinAmount(BigDecimal.ZERO)
			.userGradeMaxAmount(BigDecimal.valueOf(100000))
			.userGradePointRate(BigDecimal.valueOf(1.5))
			.build();
		UserStatus userStatus = UserStatus.builder()
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
			.userGrade(userGrade)
			.status(userStatus)
			.build();
	}

	@Test
	void signUpUser() throws Exception {
		CreateUserRequest createUserRequest = CreateUserRequest.builder()
			.name("Test User")
			.email("test@example.com")
			.password("password")
			.year(1990)
			.month(1)
			.day(1)
			.contact("01012345678")
			.build();

		CreateUserResponse createUserResponse = CreateUserResponse.builder()
			.id(1L)
			.name("Test User")
			.email("test@example.com")
			.birth(LocalDate.of(1990, 1, 1))
			.contact("01012345678")
			.userStatus("ACTIVE")
			.userGrade("REGULAR")
			.build();
		when(userService.createUser(any(CreateUserRequest.class))).thenReturn(createUserResponse);

		mockMvc.perform(post("/api/users")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(createUserRequest))
				.with(csrf())
			)
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.id").value(createUserResponse.id()))
			.andExpect(jsonPath("$.name").value(createUserResponse.name()))
			.andExpect(jsonPath("$.email").value(createUserResponse.email()))
			.andExpect(jsonPath("$.birth").value(createUserResponse.birth().toString()))
			.andExpect(jsonPath("$.contact").value(createUserResponse.contact()))
			.andExpect(jsonPath("$.userStatus").value(createUserResponse.userStatus()))
			.andExpect(jsonPath("$.userGrade").value(createUserResponse.userGrade()));
	}

	@Test
	void sendMailSignUp() throws Exception {
		String email = "test@example.com";

		when(userService.isEmailExist(anyString())).thenReturn(false);

		mockMvc.perform(post("/api/users/send-email/sign-up")
				.param("email", email)
				.with(csrf())
			)
			.andExpect(status().isOk());

		when(userService.isEmailExist(anyString())).thenReturn(true);

		mockMvc.perform(post("/api/users/send-email/sign-up")
				.param("email", email)
				.with(csrf())
			)
			.andExpect(status().isConflict());
	}

	@Test
	void sendMailDormantToActive() throws Exception {
		String email = "test@example.com";

		doNothing().when(mailService).sendMail(anyString(), anyString());

		mockMvc.perform(post("/api/users/send-email/dormant-to-active")
				.param("email", email)
				.with(csrf())
				.with(user(currentUser))
			)
			.andExpect(status().isOk());
	}

	@Test
	void checkMailSignUp() throws Exception {
		String email = "test@example.com";
		String certifyCode = "123456";

		when(mailService.checkMail(anyString(), anyString(), anyString())).thenReturn(true);

		mockMvc.perform(get("/api/users/check-email/sign-up")
				.param("email", email)
				.param("certifyCode", certifyCode)
				.with(csrf())
			)
			.andExpect(status().isOk());

		when(mailService.checkMail(anyString(), anyString(), anyString())).thenReturn(false);

		mockMvc.perform(get("/api/users/check-email/sign-up")
				.param("email", email)
				.param("certifyCode", certifyCode)
				.with(csrf())
			)
			.andExpect(status().isUnauthorized());
	}

	@Test
	void checkMailDormantToActive() throws Exception {
		String email = "test@example.com";
		String certifyCode = "123456";

		when(mailService.checkMail(anyString(), anyString(), anyString())).thenReturn(true);
		when(userRepository.findByEmail(anyString())).thenReturn(user);

		mockMvc.perform(get("/api/users/check-email/dormant-to-active")
				.param("email", email)
				.param("certifyCode", certifyCode)
				.with(csrf())
			)
			.andExpect(status().isOk());

		when(mailService.checkMail(anyString(), anyString(), anyString())).thenReturn(false);

		mockMvc.perform(get("/api/users/check-email/dormant-to-active")
				.param("email", email)
				.param("certifyCode", certifyCode)
				.with(csrf())
			)
			.andExpect(status().isUnauthorized());
	}

	@Test
	void getUsers() throws Exception {
		GetUserInfoResponse userInfoResponse1 = new GetUserInfoResponse(
			1L, "User1", "user1@example.com", LocalDateTime.now(), List.of("ROLE_USER"), "REGULAR", "ACTIVE"
		);
		GetUserInfoResponse userInfoResponse2 = new GetUserInfoResponse(
			2L, "User2", "user2@example.com", LocalDateTime.now(), List.of("ROLE_USER"), "REGULAR", "ACTIVE"
		);

		List<GetUserInfoResponse> userInfoResponseList = List.of(userInfoResponse1, userInfoResponse2);
		PageImpl<GetUserInfoResponse> userPage = new PageImpl<>(userInfoResponseList);

		when(userService.getUsers(any(Pageable.class))).thenReturn(userInfoResponseList);

		mockMvc.perform(get("/api/users")
				.param("page", "0")
				.param("size", "10")
				.with(csrf())
				.with(user(currentUser))
			)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$[0].userId").value(userInfoResponse1.userId()))
			.andExpect(jsonPath("$[0].name").value(userInfoResponse1.name()))
			.andExpect(jsonPath("$[0].email").value(userInfoResponse1.email()))
			.andExpect(jsonPath("$[0].createdAt").exists())
			.andExpect(jsonPath("$[0].roles[0]").value("ROLE_USER"))
			.andExpect(jsonPath("$[0].userGradeName").value(userInfoResponse1.userGradeName()))
			.andExpect(jsonPath("$[0].userStatusName").value(userInfoResponse1.userStatusName()))
			.andExpect(jsonPath("$[1].userId").value(userInfoResponse2.userId()))
			.andExpect(jsonPath("$[1].name").value(userInfoResponse2.name()))
			.andExpect(jsonPath("$[1].email").value(userInfoResponse2.email()))
			.andExpect(jsonPath("$[1].createdAt").exists())
			.andExpect(jsonPath("$[1].roles[0]").value("ROLE_USER"))
			.andExpect(jsonPath("$[1].userGradeName").value(userInfoResponse2.userGradeName()))
			.andExpect(jsonPath("$[1].userStatusName").value(userInfoResponse2.userStatusName()));
	}

	@Test
	void getMyUserInfo() throws Exception {
		GetMyUserInfoResponse myUserInfoResponse = GetMyUserInfoResponse.fromEntity(user);

		// JSON 직렬화 확인
		String jsonResponse = objectMapper.writeValueAsString(myUserInfoResponse);
		System.out.println("JSON Response: " + jsonResponse);

		when(userService.getMyUserInfo(any())).thenReturn(myUserInfoResponse);

		mockMvc.perform(get("/api/users/self")
				.with(user(currentUser))
				.with(csrf())
			)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.name").value(myUserInfoResponse.name()))
			.andExpect(jsonPath("$.email").value(myUserInfoResponse.email()))
			.andExpect(jsonPath("$.birth").value(myUserInfoResponse.birth().toString()))
			.andExpect(jsonPath("$.contact").value(myUserInfoResponse.contact()))
			.andExpect(jsonPath("$.createdAt").exists())
			.andExpect(jsonPath("$.roles").exists())
			.andExpect(jsonPath("$.userGradeName").value(myUserInfoResponse.userGradeName()))
			.andExpect(jsonPath("$.userStatusName").value(myUserInfoResponse.userStatusName()))
			.andExpect(jsonPath("$.points").value(myUserInfoResponse.points()));
	}

	@Test
	void updateUser() throws Exception {
		UpdateUserInfoRequest updateUserInfoRequest = UpdateUserInfoRequest.builder()
			.name("name")
			.email("updated@example.com")
			.password("newpassword")
			.birth(LocalDate.of(1991, 2, 2))
			.contact("01098765432")
			.build();

		UpdateUserInfoResponse updateUserInfoResponse = UpdateUserInfoResponse.builder()
			.name("Updated User")
			.email("updated@example.com")
			.birth(LocalDate.of(1991, 2, 2))
			.contact("01098765432")
			.build();

		when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
		when(userService.updateUserInfo(any(), any(UpdateUserInfoRequest.class)))
			.thenReturn(updateUserInfoResponse);

		mockMvc.perform(put("/api/users")
				.with(csrf())
				.with(user(currentUser))
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updateUserInfoRequest))
			)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.name").value(updateUserInfoResponse.name()))
			.andExpect(jsonPath("$.email").value(updateUserInfoResponse.email()))
			.andExpect(jsonPath("$.birth").value(updateUserInfoResponse.birth().toString()))
			.andExpect(jsonPath("$.contact").value(updateUserInfoResponse.contact()));
	}

	@Test
	@WithMockUser(username = "test@example.com", roles = "MEMBER")
	void withdrawUser() throws Exception {
		doNothing().when(userService).withdrawUser(any(CurrentUserDetails.class));

		mockMvc.perform(patch("/api/users/withdraw")
				.with(csrf())
				.with(user(currentUser))
			)
			.andExpect(status().isNoContent());
	}

	@Test
	void getTotalOrderPrice() throws Exception {
		BigDecimal totalOrderPrice = BigDecimal.valueOf(100000);

		when(orderService.getTotalOrderPrice(any())).thenReturn(totalOrderPrice);

		mockMvc.perform(get("/api/users/self/total-order-price")
				.with(csrf())
				.with(user(currentUser))
			)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$").value(totalOrderPrice.toString()));
	}

	@Test
	@WithMockUser(username = "test@example.com", roles = "MEMBER")
	void updateLastLoginAt() throws Exception {
		LocalDateTime lastLoginAt = LocalDateTime.now();

		doNothing().when(userService).updateLastLoginAt(any(CurrentUserDetails.class), any(LocalDateTime.class));

		mockMvc.perform(patch("/api/users/last-login-at")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(lastLoginAt))
			)
			.andExpect(status().isOk());
	}

	@Test
	void getUsersWithBirthday() throws Exception {
		LocalDate date = LocalDate.of(1990, 1, 1);
		List<BirthdayCouponTargetResponse> birthdayUsers = List.of(
			new BirthdayCouponTargetResponse(1L, date),
			new BirthdayCouponTargetResponse(2L, date)
		);

		when(userService.getUsersWithBirthday(any(LocalDate.class))).thenReturn(birthdayUsers);

		mockMvc.perform(get("/api/users/birthday")
				.param("date", date.toString())
				.contentType(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.length()").value(birthdayUsers.size()))
			.andExpect(jsonPath("$[0].userId").value(birthdayUsers.get(0).userId()))
			.andExpect(jsonPath("$[0].birth").value(birthdayUsers.get(0).birth().toString()))
			.andExpect(jsonPath("$[1].userId").value(birthdayUsers.get(1).userId()))
			.andExpect(jsonPath("$[1].birth").value(birthdayUsers.get(1).birth().toString()));
	}
}
