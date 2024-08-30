package com.nhnacademy.bookstoreback.eureka.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.appinfo.InstanceInfo;
import com.nhnacademy.bookstoreback.eureka.actuator.ApplicationStatus;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "actuator", description = "어플리케이션 상태 관련 API")
@Slf4j
@RestController
@RequestMapping("/api/actuator/status")
@RequiredArgsConstructor
public class ApplicationStatusController {
	private final ApplicationInfoManager applicationInfoManager;
	private final ApplicationStatus applicationStatus;

	/**
	 * 애플리케이션의 상태를 DOWN 으로 설정하고, 이를 Eureka 에 알립니다.
	 */
	@Operation(
		summary = "애플리케이션 상태를 DOWN 으로 설정",
		description = "애플리케이션의 상태를 DOWN 으로 설정하고, Eureka 에 이를 알립니다. 이 호출 이후 애플리케이션은 종료 절차를 진행합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "애플리케이션 상태가 성공적으로 DOWN 으로 설정되었습니다."),
	})
	@PostMapping
	@ResponseStatus(value = HttpStatus.OK)
	public void stopStatus() {
		applicationInfoManager.setInstanceStatus(InstanceInfo.InstanceStatus.DOWN);
		applicationStatus.stopService();
		log.info("Application stopping");
	}

}