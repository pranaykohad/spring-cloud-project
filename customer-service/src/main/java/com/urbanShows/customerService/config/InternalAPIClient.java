package com.urbanShows.customerService.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import com.urbanShows.customerService.internalAPIClient.EventServiceClient;

import io.micrometer.observation.ObservationRegistry;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class InternalAPIClient {

	private final ObservationRegistry observationRegistry;

	@Bean
	ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Bean
	EventServiceClient eventServiceClient() {
		RestClient restClient = RestClient
								.builder()
								.baseUrl("http://localhost:8082/")
								.observationRegistry(observationRegistry)
								.build();
		RestClientAdapter restClientAdapter = RestClientAdapter.create(restClient);
		HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory.builderFor(restClientAdapter).build();
		return httpServiceProxyFactory.createClient(EventServiceClient.class);
	}

}
