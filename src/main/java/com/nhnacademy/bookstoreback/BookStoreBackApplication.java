package com.nhnacademy.bookstoreback;

import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.*;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableWebSecurity
@EnableFeignClients
@EnableScheduling
@EnableAspectJAutoProxy
@ConfigurationPropertiesScan
@EnableDiscoveryClient
@EnableCaching
@EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
public class BookStoreBackApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookStoreBackApplication.class, args);
	}

}
