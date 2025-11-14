package com.urbanShows.userService.kafka;


import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class KafkaTopicConfig {
	
	@Bean
	NewTopic userLoggedInTopic() {
		return new NewTopic(KafkaTopicEnums.USER_LOGGED_IN.name(), 1, (short) 1); // 1 partition, replication factor of 1
	}
	
	@Bean
	NewTopic sendOtpToUserTopic() {
		return new NewTopic(KafkaTopicEnums.SEND_OTP_TO_USER.name(), 1, (short) 1); // 1 partition, replication factor of 1
	}

}
