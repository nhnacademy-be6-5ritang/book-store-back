package com.nhnacademy.bookstoreback.global.config;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.nhnacademy.bookstoreback.keymanager.property.RedisProperty;
import com.nhnacademy.bookstoreback.keymanager.service.KeyManagerService;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableRedisRepositories(basePackages = {"com.nhnacademy.bookstoreback.user", "com.nhnacademy.bookstoreback.address",
	"com.nhnacademy.bookstoreback.usergrade"}, redisTemplateRef = "backCacheRedisTemplate")
public class BackCacheRedisConfig {
	private final KeyManagerService keyManagerService;
	private final RedisProperty redisProperty;

	@Bean("backCacheRedisConnectionFactory")
	public RedisConnectionFactory cartRedisConnectionFactory() {
		RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
		redisStandaloneConfiguration.setHostName(keyManagerService.getSecret(redisProperty.getHost()));
		redisStandaloneConfiguration.setPort(Integer.parseInt(keyManagerService.getSecret(redisProperty.getPort())));
		redisStandaloneConfiguration.setPassword(keyManagerService.getSecret(redisProperty.getPassword()));
		redisStandaloneConfiguration.setDatabase(
			Integer.parseInt(keyManagerService.getSecret(redisProperty.getBackCacheDatabase())));
		return new LettuceConnectionFactory(redisStandaloneConfiguration);
	}

	@Bean("backCacheRedisTemplate")
	public RedisTemplate<String, Object> cartRedisTemplate(
		@Qualifier("backCacheRedisConnectionFactory") RedisConnectionFactory redisConnectionFactory) {
		RedisTemplate<String, Object> sessionRedisTemplate = new RedisTemplate<>();
		sessionRedisTemplate.setConnectionFactory(redisConnectionFactory);
		sessionRedisTemplate.setKeySerializer(new StringRedisSerializer());
		sessionRedisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
		sessionRedisTemplate.setHashKeySerializer(new StringRedisSerializer());
		sessionRedisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
		return sessionRedisTemplate;
	}

	@Bean
	public RedisCacheManager redisCacheManager(
		@Qualifier("backCacheRedisConnectionFactory") RedisConnectionFactory connectionFactory) {
		RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig()
			.entryTtl(Duration.ofMinutes(10))
			.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
			.serializeValuesWith(
				RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));

		return RedisCacheManager
			.RedisCacheManagerBuilder
			.fromConnectionFactory(connectionFactory)
			.cacheDefaults(configuration)
			.build();
	}
}
