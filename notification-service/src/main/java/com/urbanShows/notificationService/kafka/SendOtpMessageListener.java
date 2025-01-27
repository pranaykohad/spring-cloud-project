package com.urbanShows.notificationService.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SendOtpMessageListener {

	@KafkaListener(topics = "#{T(com.urbanShows.notificationService.kafka.KafkaTopicEnums).SEND_OTP_TO_USER.getKafkaTopic()}", groupId = "group_id")
	public void receiveMessage(String otpkafkaDtoStr) {
		final ObjectMapper objectMapper = new ObjectMapper();
		try {
			OtpkafkaDto otpkafkaDto = objectMapper.readValue(otpkafkaDtoStr, OtpkafkaDto.class);
			log.info("Send OTP to device: {}", otpkafkaDto.getDevice(), otpkafkaDto.getOtp());
		} catch (JsonProcessingException e) {
			log.error("Error while parsing message: {}", e);
		}
	}

}
