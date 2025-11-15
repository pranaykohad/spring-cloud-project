package com.urbanShows.userService.controller.common;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.urbanShows.userService.kafka.KafkaTopicEnums;
import com.urbanShows.userService.kafka.MessageProducer;
import com.urbanShows.userService.kafka.OtpkafkaDto;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("api/user/kafka")
@AllArgsConstructor
public class KafkaTestController {

	private MessageProducer kafkaPub;

	@GetMapping("send")
	public ResponseEntity<Boolean> sendMsg(@RequestParam String msg) {
		final OtpkafkaDto otpkafkaDto = new OtpkafkaDto("EMAIL", "123456");
		kafkaPub.sendOtpMessage(KafkaTopicEnums.SEND_OTP_TO_USER.name(), otpkafkaDto);
		return ResponseEntity.ok(true);
	}

}
