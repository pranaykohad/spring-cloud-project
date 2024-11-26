package com.urbanShows.apiGateway;

import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.server.mvc.filter.CircuitBreakerFilterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

@Configuration
public class Routes {

	private static final String FORWARD_FALLBACK_ROUTE = "forward:/fallbackRoute";

	@Value("${event.service.url}")
	private String eventServiceUrl;

	@Value("${customer.service.url}")
	private String customerServiceUrl;
	
	@Value("${notification.service.url}")
	private String notificationServiceUrl;

	@Bean
	RouterFunction<ServerResponse> customerServiceRoute() {
		return GatewayRouterFunctions.route("CUSTOMER-SERVICE")
				.route(RequestPredicates.path("api/customer/**"), HandlerFunctions.http(customerServiceUrl))
				.filter(CircuitBreakerFilterFunctions.circuitBreaker("CUSTOMER-SERVICE-CIRCUIT-BREAKER",
						URI.create(FORWARD_FALLBACK_ROUTE)))
				.build();
	}

	@Bean
	RouterFunction<ServerResponse> eventServiceRoute() {
		return GatewayRouterFunctions.route("EVENT-SERVICE")
				.route(RequestPredicates.path("api/event/**"), HandlerFunctions.http(eventServiceUrl))
				.filter(CircuitBreakerFilterFunctions.circuitBreaker("EVENT-SERVICE-CIRCUIT-BREAKER",
						URI.create(FORWARD_FALLBACK_ROUTE)))
				.build();
	}
	
	@Bean
	RouterFunction<ServerResponse> notificationServiceRoute() {
		return GatewayRouterFunctions.route("NOTIFICATION-SERVICE")
				.route(RequestPredicates.path("api/notification/**"), HandlerFunctions.http(notificationServiceUrl))
				.filter(CircuitBreakerFilterFunctions.circuitBreaker("NOTIFICATION-SERVICE-CIRCUIT-BREAKER",
						URI.create(FORWARD_FALLBACK_ROUTE)))
				.build();
	}

	@Bean
	RouterFunction<ServerResponse> customerServiceApiDocsRoute() {
		return GatewayRouterFunctions.route("CUSTOMER-SERVICE-API-DOCS")
				.route(RequestPredicates.path("customer/api-docs/**"),
						HandlerFunctions.http(customerServiceUrl + "/customer/api-docs"))
				.filter(CircuitBreakerFilterFunctions.circuitBreaker("CUSTOMER-SERVICE-API-DOCS-CIRCUIT-BREAKER",
						URI.create(FORWARD_FALLBACK_ROUTE)))
				.build();
	}

	@Bean
	RouterFunction<ServerResponse> eventServiceApiDocsRoute() {
		return GatewayRouterFunctions.route("EVENT-SERVICE-API-DOCS")
				.route(RequestPredicates.path("event/api-docs/**"),
						HandlerFunctions.http(eventServiceUrl + "/event/api-docs"))
				.filter(CircuitBreakerFilterFunctions.circuitBreaker("EVENT-SERVICE-API-DOCS-CIRCUIT-BREAKER",
						URI.create(FORWARD_FALLBACK_ROUTE)))
				.build();
	}
	
	@Bean
	RouterFunction<ServerResponse> notificationServiceApiDocsRoute() {
		return GatewayRouterFunctions.route("NOTIFICATION-SERVICE-API-DOCS")
				.route(RequestPredicates.path("notification/api-docs/**"),
						HandlerFunctions.http(notificationServiceUrl + "/notification/api-docs"))
				.filter(CircuitBreakerFilterFunctions.circuitBreaker("NOTIFICATION-SERVICE-API-DOCS-CIRCUIT-BREAKER",
						URI.create(FORWARD_FALLBACK_ROUTE)))
				.build();
	}

	@Bean
	RouterFunction<ServerResponse> fallbackRoute() {
		return route("fallbackRoute").GET("/fallbackRoute", request -> ServerResponse
				.status(HttpStatus.SERVICE_UNAVAILABLE).body("Service Unavailable, please try again later")).build();
	}

}
