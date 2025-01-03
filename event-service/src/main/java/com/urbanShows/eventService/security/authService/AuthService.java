package com.urbanShows.eventService.security.authService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.urbanShows.eventService.security.dto.UserInternalInfo;
import com.urbanShows.eventService.security.exception.ConnectionException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

	@Value("${user.service.url}")
	private String userServiceUrl;

	private final RestTemplate restTemplate;
	
	public UserInternalInfo getLoggedinAppUserInfo(String jwt) {
		try {
			final ResponseEntity<UserInternalInfo> exchange = restTemplate.exchange(
					(userServiceUrl + "/api/user/app/auth/loggedin-app-user-info"), HttpMethod.GET,
					buildHeaderEntity(jwt), UserInternalInfo.class);
			return exchange.getBody();
		} catch (Exception e) {
			throw new ConnectionException("Error in user service, please try later");
		}
	}

	public UserInternalInfo getLoggedinSystemUserInfo(String jwt) {
		try {
			final ResponseEntity<UserInternalInfo> exchange = restTemplate.exchange(
					(userServiceUrl + "/api/user/system/auth/loggedin-system-user-info"), HttpMethod.GET,
					buildHeaderEntity(jwt), UserInternalInfo.class);
			return exchange.getBody();
		} catch (Exception e) {
			throw new ConnectionException("Error in user service, please try later");
		}
	}

	public boolean validateToken(String jwt) {
		try {
			final ResponseEntity<Boolean> isValidate = restTemplate.exchange(
					(userServiceUrl + "/api/user/common/validate-token"), HttpMethod.GET, buildHeaderEntity(jwt),
					Boolean.class);
			return isValidate.getBody();
		} catch (Exception e) {
			throw new ConnectionException("Error in user service, please try later");
		}
	}
	
	public boolean checkIsUserActive(String jwt) {
		try {
			final ResponseEntity<Boolean> isValidate = restTemplate.exchange(
					(userServiceUrl + "/api/user/common/is-user-active"), HttpMethod.GET, buildHeaderEntity(jwt),
					Boolean.class);
			return isValidate.getBody();
		} catch (Exception e) {
			throw new ConnectionException("Error in user service, please try later");
		}
	}
	
	private HttpEntity<String> buildHeaderEntity(String jwt) {
		final HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + jwt);
		return new HttpEntity<>(headers);
	}

}
