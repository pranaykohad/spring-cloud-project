package com.urbanShows.apiGateway;

import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

@Configuration
public class FallbackRoutes {
	
	@Bean
	RouterFunction<ServerResponse> userFallbackRoute() {
		final Map<String, String> responseMap = new HashMap<>();
		responseMap.put("error", "User service Unavailable, please try again later");
		return route("userFallbackRoute").GET("/userFallbackRoute",
				request -> ServerResponse.status(HttpStatus.SERVICE_UNAVAILABLE).body(responseMap)).build();
	}

	@Bean
	RouterFunction<ServerResponse> eventFallbackRoute() {
		final Map<String, String> responseMap = new HashMap<>();
		responseMap.put("error", "Event service Unavailable, please try again later");
		return route("eventFallbackRoute").GET("/eventFallbackRoute",
				request -> ServerResponse.status(HttpStatus.SERVICE_UNAVAILABLE).body(responseMap)).build();
	}

	@Bean
	RouterFunction<ServerResponse> notificationFallbackRoute() {
		final Map<String, String> responseMap = new HashMap<>();
		responseMap.put("error", "Notification service Unavailable, please try again later");
		return route("notificationFallbackRoute").GET("/notificationFallbackRoute",
				request -> ServerResponse.status(HttpStatus.SERVICE_UNAVAILABLE).body(responseMap)).build();
	}

}
