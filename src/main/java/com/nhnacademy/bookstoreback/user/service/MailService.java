package com.nhnacademy.bookstoreback.user.service;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 김태환
 * 이메일 관련 기능을 구현하는 서비스 클래스입니다.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {
	private static final StringBuilder randomNumber = new StringBuilder();
	private final JavaMailSender javaMailSender;
	private final RedisTemplate<String, Object> redisTemplate;
	@Value("${spring.mail.username}")
	private String senderEmail;

	/**
	 * 6자리 랜덤 숫자를 생성하여 {@link #randomNumber}에 저장합니다.
	 */
	private static void createNumber() {
		for (int i = 0; i < 6; i++) {
			randomNumber.append((int)(Math.random() * 10));
		}
	}

	/**
	 * 이메일 메시지를 생성합니다.
	 *
	 * @param receiverEmail 수신자의 이메일 주소
	 * @param subject 이메일 제목
	 * @return 생성된 이메일 메시지
	 */
	private MimeMessage createMail(String receiverEmail, String subject) {
		createNumber();
		MimeMessage message = javaMailSender.createMimeMessage();

		try {
			message.setFrom(senderEmail);
			message.setRecipients(MimeMessage.RecipientType.TO, receiverEmail);
			message.setSubject("[5RITANG] " + subject + " 인증번호");
			String body = "";
			body += "<h3>" + subject + " 인증번호입니다.</h3>";
			body += "<h1>인증번호: " + randomNumber + "</h1>";
			body += "<h3>3분 내로 인증번호를 입력해주시기 바랍니다.</h3>";
			body += "<h3>감사합니다.</h3>";
			message.setText(body, "UTF-8", "html");
		} catch (MessagingException e) {
			log.error("메일 생성 실패", e);
		}

		return message;
	}

	/**
	 * 이메일을 전송하고, 인증 코드를 Redis에 저장합니다.
	 *
	 * @param email 수신자의 이메일 주소
	 * @param subject 이메일 제목
	 */
	public void sendMail(String email, String subject) {
		MimeMessage message = createMail(email, subject);
		javaMailSender.send(message);

		String key = getSubjectKey(subject) + email;
		redisTemplate.opsForValue().set(key, randomNumber, Duration.ofMinutes(3));

		randomNumber.setLength(0);
	}

	/**
	 * 인증 코드를 검증합니다.
	 *
	 * @param email 인증을 받을 이메일 주소
	 * @param certifyCode 검증할 인증 코드
	 * @param subject 인증 코드가 발급된 주제
	 * @return 인증 코드가 유효하면 true, 그렇지 않으면 false
	 */
	public boolean checkMail(String email, String certifyCode, String subject) {
		String key = getSubjectKey(subject) + email;
		String savedCode = (String)redisTemplate.opsForValue().get(key);

		if (savedCode == null) {
			return false;
		}

		if (savedCode.equals(certifyCode)) {
			redisTemplate.delete(key);
			return true;
		}

		return false;
	}

	/**
	 * 인증 코드의 주제에 따라 Redis 키 접두사를 반환합니다.
	 *
	 * @param subject 인증 코드가 발급된 주제
	 * @return 주제에 따른 Redis 키 접두사
	 */
	private String getSubjectKey(String subject) {
		String subjectKey = "none:";
		if ("회원가입".equals(subject)) {
			subjectKey = "SignUpEmail:";
		} else if ("휴면계정 활성화".equals(subject)) {
			subjectKey = "DormantToActiveEmail:";
		}
		return subjectKey;
	}
}
