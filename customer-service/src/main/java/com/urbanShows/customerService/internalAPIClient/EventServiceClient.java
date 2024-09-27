package com.urbanShows.customerService.internalAPIClient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.service.annotation.GetExchange;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

public interface EventServiceClient {

	Logger log = LoggerFactory.getLogger(EventServiceClient.class);

	@CircuitBreaker(name = "event-service", fallbackMethod = "fallbackMethod")
	@Retry(name = "event-service")
	@GetExchange("api/event/test")
	ResponseEntity<String> welcome();

	default ResponseEntity<String> fallbackMethod(Throwable throwable) {
		return ResponseEntity.ok("Event Service is not running, please try later");
	}

}
