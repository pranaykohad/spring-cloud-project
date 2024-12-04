package com.urbanShows.userService.kafka;

import java.util.concurrent.CompletableFuture;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class MessageProducer {

	private KafkaTemplate<String, String> stringTemplate;

	public void sendStringMessage(String kafkaTopic, String message) {
		CompletableFuture<SendResult<String, String>> send = stringTemplate.send(kafkaTopic, message);
		send.whenComplete((res, exp) -> log.info("Kafka message send successfully"));
	}

	public void sendOtpMessage(String kafkaTopic, OtpkafkaDto otpkafkaDto) {
		try {
			final String jsonString = convertObjectToString(otpkafkaDto);
			final CompletableFuture<SendResult<String, String>> send = stringTemplate.send(kafkaTopic, jsonString);
			send.whenComplete((res, exp) -> log.info("Kafka message send successfully"));
		} catch (JsonProcessingException e) {
			log.error("Error while sending OTP to phone: {}", otpkafkaDto.getPhone());
		}
	}

	private String convertObjectToString(OtpkafkaDto otpkafkaDto) throws JsonProcessingException {
		return new ObjectMapper().writeValueAsString(otpkafkaDto);
	}

}
