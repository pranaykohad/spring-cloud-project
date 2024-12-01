package com.urbanShows.userService.kafka;

public enum KafkaTopicEnums {

	USER_LOGGED_IN("USER_LOGGED_IN"),
	SEND_OTP_TO_USER("SEND_OTP_TO_USER");

	KafkaTopicEnums(String kafkaTopic) {
		this.kafkaTopic = kafkaTopic;
	}

	private String kafkaTopic;

}
