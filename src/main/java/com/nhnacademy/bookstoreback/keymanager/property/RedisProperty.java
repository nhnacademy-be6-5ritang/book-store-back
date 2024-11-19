package com.nhnacademy.bookstoreback.keymanager.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties("oritang.redis")
public class RedisProperty {
	private String host;
	private String port;
	private String password;
	private String authDatabase;
	private String cartDatabase;

	private String bookCacheDatabase;
}
