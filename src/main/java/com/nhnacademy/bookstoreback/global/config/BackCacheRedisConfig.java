package com.nhnacademy.bookstoreback.global.config;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheConfig;
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

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.impl.StdTypeResolverBuilder;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.nhnacademy.bookstoreback.keymanager.property.RedisProperty;

import lombok.RequiredArgsConstructor;

@CacheConfig
@Configuration
@RequiredArgsConstructor
@EnableRedisRepositories(basePackages = {"com.nhnacademy.bookstoreback.user", "com.nhnacademy.bookstoreback.address",
	"com.nhnacademy.bookstoreback.usergrade"}, redisTemplateRef = "backCacheRedisTemplate")
public class BackCacheRedisConfig {
	private final RedisProperty redisProperty;

	@Bean("backCacheRedisConnectionFactory")
	public RedisConnectionFactory backCacheRedisConnectionFactory() {
		RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
		redisStandaloneConfiguration.setHostName(redisProperty.getHost());
		redisStandaloneConfiguration.setPort(Integer.parseInt(redisProperty.getPort()));
		redisStandaloneConfiguration.setPassword(redisProperty.getPassword());
		redisStandaloneConfiguration.setDatabase(
			Integer.parseInt(redisProperty.getBackCacheDatabase()));
		return new LettuceConnectionFactory(redisStandaloneConfiguration);
	}

	@Bean("backCacheRedisTemplate")
	public RedisTemplate<String, Object> backCacheRedisTemplate(
		@Qualifier("backCacheRedisConnectionFactory") RedisConnectionFactory redisConnectionFactory) {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(redisConnectionFactory);
		redisTemplate.setKeySerializer(new StringRedisSerializer());

		BasicPolymorphicTypeValidator typeValidator = BasicPolymorphicTypeValidator.builder()
			.allowIfSubType(Object.class)
			.build();

		ObjectMapper objectMapper = new ObjectMapper()
			.activateDefaultTyping(typeValidator, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY)
			.registerModule(new JavaTimeModule())
			.registerModule(new ParameterNamesModule())
			.setDefaultTyping(new StdTypeResolverBuilder()
				.init(JsonTypeInfo.Id.CLASS, null)
				.inclusion(JsonTypeInfo.As.PROPERTY));

		GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer(objectMapper);

		redisTemplate.setValueSerializer(serializer);
		redisTemplate.setHashKeySerializer(new StringRedisSerializer());
		redisTemplate.setHashValueSerializer(serializer);
		return redisTemplate;
	}

	@Bean
	public RedisCacheManager redisCacheManager(
		@Qualifier("backCacheRedisConnectionFactory") RedisConnectionFactory connectionFactory) {
		BasicPolymorphicTypeValidator typeValidator = BasicPolymorphicTypeValidator.builder()
			.allowIfSubType(Object.class)
			.build();

		ObjectMapper objectMapper = new ObjectMapper()
			.activateDefaultTyping(typeValidator, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY)
			.registerModule(new JavaTimeModule())
			.registerModule(new ParameterNamesModule())
			.setDefaultTyping(new StdTypeResolverBuilder()
				.init(JsonTypeInfo.Id.CLASS, null)
				.inclusion(JsonTypeInfo.As.PROPERTY));

		GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer(objectMapper);

		RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig()
			.entryTtl(Duration.ofMinutes(10))
			.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
			.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(serializer));

		return RedisCacheManager
			.RedisCacheManagerBuilder
			.fromConnectionFactory(connectionFactory)
			.cacheDefaults(configuration)
			.build();
	}
}
