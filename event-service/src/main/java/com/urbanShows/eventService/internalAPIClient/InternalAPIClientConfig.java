package com.urbanShows.eventService.internalAPIClient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import io.micrometer.observation.ObservationRegistry;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class InternalAPIClientConfig {

	private final ObservationRegistry observationRegistry;

	@Value("${user.service.url}")
	private String userServiceUrl;

	@Bean
	UserServiceClient eventServiceClient() {
		RestClient restClient = RestClient.builder().baseUrl(userServiceUrl).observationRegistry(observationRegistry)
				.build();
		RestClientAdapter restClientAdapter = RestClientAdapter.create(restClient);
		HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory.builderFor(restClientAdapter).build();
		return httpServiceProxyFactory.createClient(UserServiceClient.class);
	}
}
