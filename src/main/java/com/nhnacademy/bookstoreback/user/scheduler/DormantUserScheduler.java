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

/**
 * @author 김태환
 * 비활성 사용자 상태를 처리하기 위한 스케줄러 클래스입니다.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DormantUserScheduler {
	private final UserRepository userRepository;
	private final UserStatusRepository userStatusRepository;

	/**
	 * 매일 자정에 실행되어 90일 이상 로그인하지 않은 사용자들의 상태를 'DORMANT'로 업데이트합니다.
	 * <p>
	 * 이 메서드는 다음과 같은 절차를 따릅니다:
	 * <ul>
	 *     <li>모든 사용자를 조회합니다.</li>
	 *     <li>'DORMANT' 상태가 존재하지 않으면 새로 생성합니다.</li>
	 *     <li>마지막 로그인 시점이 현재 시점에서 90일을 초과하고 현재 상태가 'ACTIVE'인 사용자를 비활성 상태로 변경합니다.</li>
	 *     <li>상태가 변경된 사용자 정보를 저장합니다.</li>
	 * </ul>
	 */
	@Scheduled(cron = "0 0 0 * * *")    // 매 5초마다 실행
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
