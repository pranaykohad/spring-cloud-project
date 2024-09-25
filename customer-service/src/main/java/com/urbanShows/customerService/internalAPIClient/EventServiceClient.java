package com.urbanShows.customerService.internalAPIClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "EVENT-SERVICE", url = "http://localhost:8082")
public interface EventServiceClient {

	@GetMapping("api/event/test")
	ResponseEntity<String> welcome();

}
