package com.nhnacademy.bookstoreback.keymanager;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.nhnacademy.bookstoreback.keymanager.property.ImageManagerProperty;
import com.nhnacademy.bookstoreback.keymanager.property.MysqlProperty;
import com.nhnacademy.bookstoreback.keymanager.property.NaverApiProperty;
import com.nhnacademy.bookstoreback.keymanager.property.RedisProperty;

class PropertyTest {

	private ImageManagerProperty imageManagerProperty;
	private MysqlProperty mysqlProperty;
	private NaverApiProperty naverApiProperty;
	private RedisProperty redisProperty;

	@BeforeEach
	void setUp() {
		imageManagerProperty = new ImageManagerProperty();
		imageManagerProperty.setAppKey("testAppKey");
		imageManagerProperty.setSecretKey("testSecretKey");

		mysqlProperty = new MysqlProperty();
		mysqlProperty.setUrl("jdbc:mysql://localhost:3306/testdb");
		mysqlProperty.setUsername("testUser");
		mysqlProperty.setPassword("testPassword");

		naverApiProperty = new NaverApiProperty();
		naverApiProperty.setId("testId");
		naverApiProperty.setSecret("testSecret");

		redisProperty = new RedisProperty();
		redisProperty.setHost("localhost");
		redisProperty.setPort("6379");
		redisProperty.setPassword("testPassword");
		redisProperty.setAuthDatabase("0");
		redisProperty.setCartDatabase("1");
	}

	@Test
	void testImageManagerProperty() {
		assertEquals("testAppKey", imageManagerProperty.getAppKey());
		assertEquals("testSecretKey", imageManagerProperty.getSecretKey());
	}

	@Test
	void testMysqlProperty() {
		assertEquals("jdbc:mysql://localhost:3306/testdb", mysqlProperty.getUrl());
		assertEquals("testUser", mysqlProperty.getUsername());
		assertEquals("testPassword", mysqlProperty.getPassword());
	}

	@Test
	void testNaverApiProperty() {
		assertEquals("testId", naverApiProperty.getId());
		assertEquals("testSecret", naverApiProperty.getSecret());
	}

	@Test
	void testRedisProperty() {
		assertEquals("localhost", redisProperty.getHost());
		assertEquals("6379", redisProperty.getPort());
		assertEquals("testPassword", redisProperty.getPassword());
		assertEquals("0", redisProperty.getAuthDatabase());
		assertEquals("1", redisProperty.getCartDatabase());
	}
}