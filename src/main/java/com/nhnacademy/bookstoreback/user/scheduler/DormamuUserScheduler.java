package com.nhnacademy.bookstoreback.user.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DormamuUserScheduler {
	// 매 5초마다 실행
	@Scheduled(cron = "0/5 * * * * *")
	public void dormamuUser() {
		log.info("Dormamu User Scheduler");
	}
}
