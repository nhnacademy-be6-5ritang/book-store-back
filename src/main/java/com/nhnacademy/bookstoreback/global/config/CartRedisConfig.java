// package com.nhnacademy.bookstoreback.global.config;
//
// import org.springframework.beans.factory.annotation.Qualifier;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.data.redis.connection.RedisConnectionFactory;
// import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
// import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
// import org.springframework.data.redis.core.RedisTemplate;
// import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
// import org.springframework.data.redis.serializer.StringRedisSerializer;
//
// @Configuration
// public class CartRedisConfig {
// 	@Value("${spring.data.redis.host}")
// 	private String host;
// 	@Value("${spring.data.redis.port}")
// 	private int port;
// 	@Value("${spring.data.redis.password}")
// 	private String password;
//
// 	@Bean("cartRedisConnectionFactory")
// 	public RedisConnectionFactory cartRedisConnectionFactory() {
// 		RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
// 		redisStandaloneConfiguration.setHostName(host);
// 		redisStandaloneConfiguration.setPort(port);
// 		redisStandaloneConfiguration.setPassword(password);
// 		redisStandaloneConfiguration.setDatabase(25);
// 		return new LettuceConnectionFactory(redisStandaloneConfiguration);
// 	}
//
// 	@Bean("cartRedisTemplate")
// 	public RedisTemplate<String, Object> cartRedisTemplate(
// 		@Qualifier("cartRedisConnectionFactory") RedisConnectionFactory redisConnectionFactory) {
// 		RedisTemplate<String, Object> sessionRedisTemplate = new RedisTemplate<>();
// 		sessionRedisTemplate.setConnectionFactory(redisConnectionFactory);
// 		sessionRedisTemplate.setKeySerializer(new StringRedisSerializer());
// 		sessionRedisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
// 		sessionRedisTemplate.setHashKeySerializer(new StringRedisSerializer());
// 		sessionRedisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
// 		return sessionRedisTemplate;
// 	}
// }
