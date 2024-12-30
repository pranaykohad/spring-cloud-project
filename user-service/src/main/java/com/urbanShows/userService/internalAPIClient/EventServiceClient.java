package com.urbanShows.userService.internalAPIClient;

import org.springframework.http.ResponseEntity;
import org.springframework.web.service.annotation.GetExchange;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

public interface EventServiceClient {

	@CircuitBreaker(name = "event-service", fallbackMethod = "fallbackMethod")
	@Retry(name = "event-service")
	@GetExchange("api/event/test")
	ResponseEntity<String> welcome();

	default ResponseEntity<String> fallbackMethod(Throwable throwable) {
		return ResponseEntity.ok("Event Service is not running, please try later");
	}

}
