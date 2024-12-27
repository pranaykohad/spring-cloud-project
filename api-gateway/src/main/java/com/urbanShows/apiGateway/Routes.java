package com.urbanShows.apiGateway;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.server.mvc.filter.CircuitBreakerFilterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

@Configuration
public class Routes {

	private static final String USER_FALLBACK_ROUTE = "forward:/userFallbackRoute";
	private static final String EVENT_FALLBACK_ROUTE = "forward:/eventFallbackRoute";
	private static final String NOTIFICATION_FALLBACK_ROUTE = "forward:/notificationFallbackRoute";

	@Value("${event.service.url}")
	private String eventServiceUrl;

	@Value("${user.service.url}")
	private String userServiceUrl;

	@Value("${notification.service.url}")
	private String notificationServiceUrl;

	@Bean
	RouterFunction<ServerResponse> userServiceRoute() {
		return GatewayRouterFunctions.route("USER-SERVICE")
				.route(RequestPredicates.path("api/user/**"), HandlerFunctions.http(userServiceUrl))
				.filter(CircuitBreakerFilterFunctions.circuitBreaker("USER-SERVICE-CIRCUIT-BREAKER",
						URI.create(USER_FALLBACK_ROUTE)))
				.build();
	}

	@Bean
	RouterFunction<ServerResponse> eventServiceRoute() {
		return GatewayRouterFunctions.route("EVENT-SERVICE")
				.route(RequestPredicates.path("api/event/**"), HandlerFunctions.http(eventServiceUrl))
				.filter(CircuitBreakerFilterFunctions.circuitBreaker("EVENT-SERVICE-CIRCUIT-BREAKER",
						URI.create(EVENT_FALLBACK_ROUTE)))
				.build();
	}

	@Bean
	RouterFunction<ServerResponse> notificationServiceRoute() {
		return GatewayRouterFunctions.route("NOTIFICATION-SERVICE")
				.route(RequestPredicates.path("api/notification/**"), HandlerFunctions.http(notificationServiceUrl))
				.filter(CircuitBreakerFilterFunctions.circuitBreaker("NOTIFICATION-SERVICE-CIRCUIT-BREAKER",
						URI.create(NOTIFICATION_FALLBACK_ROUTE)))
				.build();
	}

	@Bean
	RouterFunction<ServerResponse> userServiceApiDocsRoute() {
		return GatewayRouterFunctions.route("USER-SERVICE-API-DOCS")
				.route(RequestPredicates.path("user/api-docs/**"),
						HandlerFunctions.http(userServiceUrl + "/user/api-docs"))
				.filter(CircuitBreakerFilterFunctions.circuitBreaker("USER-SERVICE-API-DOCS-CIRCUIT-BREAKER",
						URI.create(USER_FALLBACK_ROUTE)))
				.build();
	}

	@Bean
	RouterFunction<ServerResponse> eventServiceApiDocsRoute() {
		return GatewayRouterFunctions.route("EVENT-SERVICE-API-DOCS")
				.route(RequestPredicates.path("event/api-docs/**"),
						HandlerFunctions.http(eventServiceUrl + "/event/api-docs"))
				.filter(CircuitBreakerFilterFunctions.circuitBreaker("EVENT-SERVICE-API-DOCS-CIRCUIT-BREAKER",
						URI.create(EVENT_FALLBACK_ROUTE)))
				.build();
	}

	@Bean
	RouterFunction<ServerResponse> notificationServiceApiDocsRoute() {
		return GatewayRouterFunctions.route("NOTIFICATION-SERVICE-API-DOCS")
				.route(RequestPredicates.path("notification/api-docs/**"),
						HandlerFunctions.http(notificationServiceUrl + "/notification/api-docs"))
				.filter(CircuitBreakerFilterFunctions.circuitBreaker("NOTIFICATION-SERVICE-API-DOCS-CIRCUIT-BREAKER",
						URI.create(NOTIFICATION_FALLBACK_ROUTE)))
				.build();
	}

}
