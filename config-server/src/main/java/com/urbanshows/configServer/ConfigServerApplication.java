package com.urbanshows.configServer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.core.env.Environment;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
@EnableConfigServer
public class ConfigServerApplication {

	private final Environment environment;

	ConfigServerApplication(Environment environment) {
		this.environment = environment;
	}

	@Value("${server.port}")
	private String port;

	@Value("${springdoc.swagger-ui.path}")
	private String swaggerPath;

	@Value("${event.service.url}")
	private String eventServiceUrl;

	@Value("${user.service.url}")
	private String userServiceUrl;

	@Value("${notification.service.url}")
	private String notificationServiceUrl;

	@Value("${spring.kafka.producer.bootstrap-servers}")
	private String kafkaConsumerServer;

	@PostConstruct
	public void postConstruct() {
		log.info("Config Service is started on port: {}, swagger URL: {}", port, swaggerPath);
		log.info("User Service URL: {}", eventServiceUrl);
		log.info("Event Service URL: {}", userServiceUrl);
		log.info("Notification Service URL: {}, Kafka Consumer URL: {}", notificationServiceUrl);
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
