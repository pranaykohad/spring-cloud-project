package com.urbanShows.eventService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@EntityScan("com.urbanShows.eventService.entity")
@EnableJpaRepositories("com.urbanShows.eventService.repository")
@Slf4j
public class EventServiceApplication {

	@Value("${server.port}")
	private String port;

	@Value("${springdoc.swagger-ui.path}")
	private String swaggerPath;

	public static void main(String[] args) {
		SpringApplication.run(EventServiceApplication.class, args);
		log.info("Event server is started...");

	}

	@PostConstruct
	public void postConstruct() {
		log.info("Swagger url: http://localhost:" + port + swaggerPath);
	}

}
