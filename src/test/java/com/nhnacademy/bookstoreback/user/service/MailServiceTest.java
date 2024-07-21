package com.nhnacademy.bookstoreback.user.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;
import java.time.Duration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.javamail.JavaMailSender;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

class MailServiceTest {

	@Mock
	private JavaMailSender javaMailSender;

	@Mock
	private RedisTemplate<String, Object> redisTemplate;

	@Mock
	private ValueOperations<String, Object> valueOperations;

	@InjectMocks
	private MailService mailService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		when(redisTemplate.opsForValue()).thenReturn(valueOperations);
	}

	@Test
	void testGetSubjectKey() throws Exception {
		Method getSubjectKeyMethod = MailService.class.getDeclaredMethod("getSubjectKey", String.class);
		getSubjectKeyMethod.setAccessible(true);

		String result = (String)getSubjectKeyMethod.invoke(mailService, "Invalid Subject");

		assertThat(result).isEqualTo("none:");
	}

	@Test
	void testSendMail() throws MessagingException {
		MimeMessage mimeMessage = mock(MimeMessage.class);
		when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

		String receiverEmail = "test@example.com";
		String subject = "회원가입";

		mailService.sendMail(receiverEmail, subject);

		verify(javaMailSender, times(1)).send(mimeMessage);
		verify(valueOperations, times(1)).set(anyString(), any(), eq(Duration.ofMinutes(3)));
	}

	@Test
	void testCheckMailTrue() {
		String email = "test@example.com";
		String certifyCode = "123456";
		String subject = "회원가입";

		when(valueOperations.get(anyString())).thenReturn(certifyCode);

		boolean result = mailService.checkMail(email, certifyCode, subject);

		assertThat(result).isTrue();
		verify(redisTemplate, times(1)).delete(anyString());
	}

	@Test
	void testCheckMailFalse() {
		String email = "test@example.com";
		String certifyCode = "123456";
		String subject = "회원가입";

		when(valueOperations.get(anyString())).thenReturn("654321");

		boolean result = mailService.checkMail(email, certifyCode, subject);

		assertThat(result).isFalse();
	}

	@Test
	void testCheckMailCodeNotFound() {
		String email = "test@example.com";
		String certifyCode = "123456";
		String subject = "회원가입";

		when(valueOperations.get(anyString())).thenReturn(null);

		boolean result = mailService.checkMail(email, certifyCode, subject);

		assertThat(result).isFalse();
	}

	@Test
	void testGetSubjectKeyForSignup() throws Exception {
		Method getSubjectKeyMethod = MailService.class.getDeclaredMethod("getSubjectKey", String.class);
		getSubjectKeyMethod.setAccessible(true);

		String result = (String)getSubjectKeyMethod.invoke(mailService, "회원가입");

		assertThat(result).isEqualTo("SignUpEmail:");
	}

	@Test
	void testGetSubjectKeyForDormantToActive() throws Exception {
		Method getSubjectKeyMethod = MailService.class.getDeclaredMethod("getSubjectKey", String.class);
		getSubjectKeyMethod.setAccessible(true);

		String result = (String)getSubjectKeyMethod.invoke(mailService, "휴면계정 활성화");

		assertThat(result).isEqualTo("DormantToActiveEmail:");
	}

	@Test
	void testGetSubjectKeyForInvalidSubject() throws Exception {
		Method getSubjectKeyMethod = MailService.class.getDeclaredMethod("getSubjectKey", String.class);
		getSubjectKeyMethod.setAccessible(true);

		String result = (String)getSubjectKeyMethod.invoke(mailService, "잘못된주제");

		assertThat(result).isEqualTo("none:");
	}
}
