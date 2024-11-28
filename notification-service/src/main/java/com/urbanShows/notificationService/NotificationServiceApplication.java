package com.urbanShows.notificationService;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@EntityScan("com.urbanShows.notificationService.entity")
@EnableJpaRepositories("com.urbanShows.notificationService.repository")
@Slf4j
public class NotificationServiceApplication {

	private final Environment environment;

	NotificationServiceApplication(Environment environment) {
		this.environment = environment;
	}

	@Value("${server.port}")
	private String port;

	@Value("${springdoc.swagger-ui.path}")
	private String swaggerPath;
	
	@Value("${customer.service.url}")
	private String customerServiceUrl;
	
	@Value("${event.service.url}")
	private String eventServiceUrl;
	
	@Value("${spring.kafka.consumer.bootstrap-servers}")
	private String kafkaConsumerServer;
	
	public static void main(String[] args) {
		SpringApplication.run(NotificationServiceApplication.class, args);
	}

	@PostConstruct
	public void postConstruct() {
		log.info("Notification Service is started on port: {}, swagger URL: {}" , port , swaggerPath);
		log.info("Customer Service URL: {}, Kafka Consumer URL: {}" , customerServiceUrl , kafkaConsumerServer);
		log.info("Event Service URL: {}, Kafka Consumer URL: {}" , eventServiceUrl , kafkaConsumerServer);
		final String[] activeProfiles = environment.getActiveProfiles();
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
