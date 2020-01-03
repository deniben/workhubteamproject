package com.spring.configuration;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.spring.constants.SecurityConstants;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.any())
				.build()
				.apiInfo(apiInfo())
				.securityContexts(Collections.singletonList(securityContext()))
				.securitySchemes(Collections.singletonList(apiKey()));
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("WorkHub API")
				.description("REST API for WorkHub service. JWT token security")
				.version("1.0 RELEASE")
				.contact(new Contact("Work Hub (CEO)", "www.workhub.com", "workhub.developing@gmail.com"))
				.build();
	}

	private ApiKey apiKey() {
		return new ApiKey("JWT", SecurityConstants.TOKEN_HEADER, "header");
	}

	private SecurityContext securityContext() {
		return SecurityContext.builder()
				.securityReferences(securityReferences())
				.forPaths(PathSelectors.any())
				.build();
	}

	private List<SecurityReference> securityReferences() {
		AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
		return Arrays.asList(SecurityReference.builder()
				.reference("JWT")
				.scopes(new AuthorizationScope[] { authorizationScope })
				.build());
	}

}
