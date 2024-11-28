package com.urbanShows.userService.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.urbanShows.userService.config.MessageProducer;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("api/user/kafka")
@AllArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class KafkaTestController {

	private MessageProducer kafkaPub;

	@GetMapping("send")
	public ResponseEntity<Boolean> sendMsg(@RequestParam String msg) {
		kafkaPub.sendMessage(msg);
		return ResponseEntity.ok(true);
	}

}
