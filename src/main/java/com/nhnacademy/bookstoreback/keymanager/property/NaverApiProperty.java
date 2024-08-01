package com.nhnacademy.bookstoreback.keymanager.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ConfigurationProperties("oritang.naver.client")
public class NaverApiProperty {
	private String id;
	private String secret;
}
