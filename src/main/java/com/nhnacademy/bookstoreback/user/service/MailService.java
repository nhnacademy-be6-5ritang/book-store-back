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

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {
	private static final StringBuilder randomNumber = new StringBuilder();
	private final JavaMailSender javaMailSender;
	private final RedisTemplate<String, Object> redisTemplate;
	@Value("${spring.mail.username}")
	private String senderEmail;

	private static void createNumber() {
		for (int i = 0; i < 6; i++) {
			randomNumber.append((int)(Math.random() * 10));
		}
	}

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

	public void sendMail(String email, String subject) {
		MimeMessage message = createMail(email, subject);
		javaMailSender.send(message);

		String key = getSubjectKey(subject) + email;
		redisTemplate.opsForValue().set(key, randomNumber, Duration.ofMinutes(3));

		randomNumber.setLength(0);
	}

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
