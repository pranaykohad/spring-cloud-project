package com.urbanShows.eventService.kafka.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class KafkaConsumer {

	@KafkaListener(topics = "CUSTOMER_LOGGED_IN", groupId = "group_id")
	public void receiveMessage(String msg) {
		log.info("Kafka message recived: {}" , msg);
	}

}
