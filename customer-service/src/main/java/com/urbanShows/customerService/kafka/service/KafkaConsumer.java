package com.urbanShows.customerService.kafka.config;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaSub {

	@KafkaListener(topics = "CUSTOMER_LOGGED_IN", groupId = "group_id")
	public void receiveMessage(String msg) {
		System.out.println("message received: " + msg);
	}

}
