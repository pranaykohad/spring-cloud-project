package com.urbanShows.customerService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@EntityScan("com.urbanShows.customerService.entity")
@EnableJpaRepositories("com.urbanShows.customerService.repository")
@Slf4j
public class CustomerServiceApplication {

	private final Environment environment;

	CustomerServiceApplication(Environment environment) {
		this.environment = environment;
	}

	@Value("${server.port}")
	private String port;

	@Value("${springdoc.swagger-ui.path}")
	private String swaggerPath;
	
	@Value("${event.service.url}")
	private String eventServiceUrl;
	
	@Value("${spring.kafka.producer.bootstrap-servers}")
	private String kafkaConsumerServer;

	public static void main(String[] args) {
		SpringApplication.run(CustomerServiceApplication.class, args);
	}

	@PostConstruct
	public void postConstruct() {
		log.info("Customer Service is started on port: {}, swagger URL: {}" , port , swaggerPath);
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
