package com.urbanShows.apiGateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class ApiGatewayApplication {

	private final Environment environment;

	public ApiGatewayApplication(Environment environment) {
		this.environment = environment;
	}

	public static void main(String[] args) {
		SpringApplication.run(ApiGatewayApplication.class, args);
	}

	@PostConstruct
	void postConstruct() {
		String[] activeProfiles = environment.getActiveProfiles();
		System.out.println("API Gateway is started...");
		if (activeProfiles.length == 0) {
			System.out.println("No active profile is set.");
		} else {
			System.out.println("Active profiles: ");
			for (String profile : activeProfiles) {
				System.out.println(profile);
			}
		}
	}

}
