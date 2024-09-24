package com.urbanShows.customerService.internalAPIClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "EVENT-SERVICE")
public interface EventServiceClient {

	@GetMapping("customer/test")
	ResponseEntity<String> welcome();

}
