package com.nhnacademy.bookstoreback.global.config;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import io.swagger.v3.oas.models.OpenAPI;

class SwaggerConfigTest {

	private final ApplicationContextRunner contextRunner = new ApplicationContextRunner();

	@Test
	void testCustomOpenAPI() {
		contextRunner.withUserConfiguration(SwaggerConfig.class).run(context -> {
			OpenAPI openAPI = context.getBean(OpenAPI.class);

			assertThat(openAPI).isNotNull();
			assertThat(openAPI.getInfo()).isNotNull();
			assertThat(openAPI.getInfo().getTitle()).isEqualTo("Back API");
			assertThat(openAPI.getInfo().getVersion()).isEqualTo("1.0");
			assertThat(openAPI.getInfo().getDescription()).isEqualTo("Back API 명세서");
		});
	}

	@Test
	void testGroupedOpenApi() {
		contextRunner.withUserConfiguration(SwaggerConfig.class).run(context -> {
			GroupedOpenApi groupedOpenApi = context.getBean(GroupedOpenApi.class);

			assertThat(groupedOpenApi).isNotNull();
			assertThat(groupedOpenApi.getGroup()).isEqualTo("back-api");
			assertThat(groupedOpenApi.getPathsToMatch()).containsExactly("/api/**");
			assertThat(groupedOpenApi.getPackagesToScan()).containsExactly("com.nhnacademy.bookstoreback");
		});
	}
}