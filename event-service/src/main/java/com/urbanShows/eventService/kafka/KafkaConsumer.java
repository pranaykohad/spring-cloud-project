package com.urbanShows.eventService.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

//@Service
@Slf4j
public class KafkaConsumer {
	
	@KafkaListener(topics = "#{T(com.urbanShows.eventService.kafka.KafkaTopicEnums).USER_LOGGED_IN.getKafkaTopic()}", groupId = "group_id")
	public void receiveMessage(String msg) {
		log.info("Kafka message recived: {}" , msg);
	}

}
