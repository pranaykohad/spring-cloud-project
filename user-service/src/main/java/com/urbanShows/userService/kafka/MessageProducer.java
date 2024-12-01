package com.urbanShows.userService.kafka;

import java.util.concurrent.CompletableFuture;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class MessageProducer {

	private KafkaTemplate<String, String> kafkaTemplate;

	public void sendMessage(String kafkaTopic, String message) {
		CompletableFuture<SendResult<String, String>> send = kafkaTemplate.send(kafkaTopic, message);
		send.whenComplete((res, exp) -> log.info("Kafka message send successfully"));
	}

}
