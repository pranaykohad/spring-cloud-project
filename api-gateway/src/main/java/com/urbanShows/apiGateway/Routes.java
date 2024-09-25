package com.urbanShows.apiGateway;

import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

@Configuration
public class Routes {

	@Bean
	RouterFunction<ServerResponse> customerServiceRoute() {
		return GatewayRouterFunctions.route("CUSTOMER-SERVICE")
				.route(RequestPredicates.path("api/customer/**"), HandlerFunctions.http("http://localhost:8081")).build(); 
	}
	
	@Bean
	RouterFunction<ServerResponse> eventServiceRoute() {
		return GatewayRouterFunctions.route("EVENT-SERVICE")
				.route(RequestPredicates.path("api/event/**"), HandlerFunctions.http("http://localhost:8082")).build();
	}
	
	@Bean
	RouterFunction<ServerResponse> customerServiceApiDocsRoute() {
		return GatewayRouterFunctions.route("CUSTOMER-SERVICE-API-DOCS")
				.route(RequestPredicates.path("customer/api-docs/**"), HandlerFunctions.http("http://localhost:8081/customer/api-docs")).build(); 
	}
	
	@Bean
	RouterFunction<ServerResponse> eventServiceApiDocsRoute() {
		return GatewayRouterFunctions.route("EVENT-SERVICE-API-DOCS")
				.route(RequestPredicates.path("event/api-docs/**"), HandlerFunctions.http("http://localhost:8082/event/api-docs")).build();
	}

}
