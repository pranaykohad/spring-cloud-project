package com.urbanShows.customerService.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfig {

	@Bean
	NewTopic customerLoggedInTopic() {
		return new NewTopic("CUSTOMER_LOGGED_IN", 1, (short) 1); // 1 partition, replication factor of 1
	}

}
