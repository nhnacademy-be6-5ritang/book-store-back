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
@EnableRedisRepositories(basePackages = {
	"com.nhnacademy.bookstoreback.product"}, redisTemplateRef = "bookCacheRedisTemplate")
public class BookCacheRedisConfig {
	private final RedisProperty redisProperty;

	@Bean("bookCacheRedisConnectionFactory")
	public RedisConnectionFactory bookCacheRedisConnectionFactory() {
		RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
		redisStandaloneConfiguration.setHostName(redisProperty.getHost());
		redisStandaloneConfiguration.setPort(Integer.parseInt(redisProperty.getPort()));
		redisStandaloneConfiguration.setPassword(redisProperty.getPassword());
		redisStandaloneConfiguration.setDatabase(
			Integer.parseInt(redisProperty.getBookCacheDatabase()));
		return new LettuceConnectionFactory(redisStandaloneConfiguration);
	}

	@Bean("bookCacheRedisTemplate")
	public RedisTemplate<String, Object> bookCacheRedisTemplate(
		@Qualifier("bookCacheRedisConnectionFactory") RedisConnectionFactory redisConnectionFactory) {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(redisConnectionFactory);
		redisTemplate.setKeySerializer(new StringRedisSerializer());

		GenericJackson2JsonRedisSerializer serializer = getSerializer();

		redisTemplate.setValueSerializer(serializer);
		redisTemplate.setHashKeySerializer(new StringRedisSerializer());
		redisTemplate.setHashValueSerializer(serializer);
		return redisTemplate;
	}

	@Bean
	public RedisCacheManager redisCacheManager(
		@Qualifier("bookCacheRedisConnectionFactory") RedisConnectionFactory connectionFactory) {

		GenericJackson2JsonRedisSerializer serializer = getSerializer();

		RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig()
			.entryTtl(Duration.ofDays(1))
			.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
			.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(serializer));

		return RedisCacheManager
			.RedisCacheManagerBuilder
			.fromConnectionFactory(connectionFactory)
			.cacheDefaults(configuration)
			.build();
	}

	private GenericJackson2JsonRedisSerializer getSerializer() {
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

		return new GenericJackson2JsonRedisSerializer(objectMapper);
	}
}
