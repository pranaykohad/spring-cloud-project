package com.urbanShows.eventService.internalAPIClient;

import org.springframework.http.ResponseEntity;

public interface UserServiceClient {

//	@CircuitBreaker(name = "user-service", fallbackMethod = "fallbackMethod")
//	@Retry(name = "user-service")
//	@GetExchange("api/user/system/auth/load-user-by-username")
//	ResponseEntity<UserDetails> loadUserByUsername(@RequestParam String userName);

//	@CircuitBreaker(name = "user-service", fallbackMethod = "fallbackMethod")
//	@Retry(name = "user-service")
//	@GetExchange(value = "api/user/system/auth/load-user-by-id")
//	ResponseEntity<UserDetailsDto> loadUserById(@RequestParam String id);

//	@CircuitBreaker(name = "user-service", fallbackMethod = "fallbackMethod")
//	@Retry(name = "user-service")
//	@GetExchange("api/user/system/auth/validate-token")
//	ResponseEntity<Boolean> validateToken(@RequestParam String token, @RequestParam UserDetails userDetails);
//
//	@CircuitBreaker(name = "user-service", fallbackMethod = "fallbackMethod")
//	@Retry(name = "user-service")
//	@GetExchange("api/user/system/auth/extract-token")
//	ResponseEntity<String> extractUsername(@RequestParam String token);

	default ResponseEntity<String> fallbackMethod(Throwable throwable) {
		return ResponseEntity.ok("User Service is not running, please try later");
	}

}
