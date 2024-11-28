package com.urbanShows.apiGateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class ApiGatewayApplication {

	private final Environment environment;

	@Value("${server.port}")
	private String port;

	@Value("${user.service.url}")
	private String userServiceUrl;

	@Value("${event.service.url}")
	private String eventServiceUrl;

	@Value("${notification.service.url}")
	private String notificationServiceUrl;

	public ApiGatewayApplication(Environment environment) {
		this.environment = environment;
	}

	public static void main(String[] args) {
		SpringApplication.run(ApiGatewayApplication.class, args);
	}

	@PostConstruct
	void postConstruct() {
		log.info("API Gateway is started on port: {}", port);
		log.info("User Service URL: {}", userServiceUrl);
		log.info("Event Service URL: {}", eventServiceUrl);
		log.info("Notification Service URL: {}", notificationServiceUrl);
		String[] activeProfiles = environment.getActiveProfiles();
		if (activeProfiles.length == 0) {
			log.info("No active profile is set.");
		} else {
			log.info("Active profiles: ");
			for (String profile : activeProfiles) {
				log.info(profile);
			}
		}
	}

}
