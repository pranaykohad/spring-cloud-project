package com.urbanShows.notificationService.kafka;

public enum KafkaTopicEnums {

	USER_LOGGED_IN("USER_LOGGED_IN"),
	SEND_OTP_TO_USER("SEND_OTP_TO_USER");

	KafkaTopicEnums(String kafkaTopic) {
		this.kafkaTopic = kafkaTopic;
	}

	private final String kafkaTopic;
	
	public String getKafkaTopic() {
        return kafkaTopic;
    }

}
