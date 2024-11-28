package com.urbanShows.userService.config;


import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfig {
	
	// add different topics here

	@Bean
	NewTopic userLoggedInTopic() {
		return new NewTopic("USER_LOGGED_IN", 1, (short) 1); // 1 partition, replication factor of 1
	}

}
