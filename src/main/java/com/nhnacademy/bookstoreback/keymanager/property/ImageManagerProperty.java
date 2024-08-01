package com.nhnacademy.bookstoreback.keymanager.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ConfigurationProperties("oritang.image-manager")
public class ImageManagerProperty {
	private String appKey;
	private String secretKey;
}
