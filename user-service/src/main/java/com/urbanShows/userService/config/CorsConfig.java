package com.urbanShows.userService.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

// @Bean uncomment -> Work direct swagger, API Gateway swagger, angular calls direct, not angular calls via API Gateway
// @Bean comment -> Work only direct swagger, angular calls via API Gateway

@Configuration
public class CorsConfig {

//	@Bean
//	CorsFilter corsFilter() {
//		final CorsConfiguration config = new CorsConfiguration();
//		config.setAllowedOrigins(Arrays.asList("http://localhost:8083", "http://localhost:4200"));
//		config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
//		config.setAllowedHeaders(Arrays.asList("*"));
//		config.setAllowCredentials(false);
//		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//		source.registerCorsConfiguration("/**", config);
//		return new CorsFilter(source);
//	}

}