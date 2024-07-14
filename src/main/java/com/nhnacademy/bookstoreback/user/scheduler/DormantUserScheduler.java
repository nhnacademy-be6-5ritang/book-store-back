package com.nhnacademy.bookstoreback.user.scheduler;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.nhnacademy.bookstoreback.user.domain.entity.User;
import com.nhnacademy.bookstoreback.user.repository.UserRepository;
import com.nhnacademy.bookstoreback.userstatus.domain.entity.UserStatus;
import com.nhnacademy.bookstoreback.userstatus.repository.UserStatusRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class DormantUserScheduler {
	private final UserRepository userRepository;
	private final UserStatusRepository userStatusRepository;

	// 매 5초마다 실행
	@Scheduled(cron = "0 0 0 * * *")
	public void dormantUser() {
		List<User> users = userRepository.findAll();
		UserStatus dormantUserStatus = userStatusRepository.findByUserStatusName("DORMANT")
			.orElseGet(() -> userStatusRepository.save(UserStatus.builder().userStatusName("DORMANT").build()));

		for (User user : users) {
			if (user.getLastLoginAt().isBefore(LocalDateTime.now().minusDays(90))
				&& user.getStatus().getUserStatusName().equals("ACTIVE")) {
				user.updateUserStatus(dormantUserStatus);
				userRepository.save(user);
			}
		}
	}
}
