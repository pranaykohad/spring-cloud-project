package com.urbanShows.eventService.security.authService;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.urbanShows.eventService.security.dto.UserInternalInfo;
import com.urbanShows.eventService.security.exception.ConnectionException;
import com.urbanShows.eventService.security.exception.UnauthorizedException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

	@Value("${user.service.url}")
	private String userServiceUrl;

	private final RestTemplate restTemplate;

	public Boolean isOrganizerActive(String jwt, String userName) {
		try {
			final ResponseEntity<Boolean> exchange = restTemplate.exchange(
					(userServiceUrl + "/api/user/common/is-valid-organizer?userName=" + userName), HttpMethod.GET,
					buildHeaderEntity(jwt), Boolean.class);
			return exchange.getBody();
		} catch (Exception e) {
			throw new ConnectionException(extractMessage(e.getMessage()));
		}
	}

	public Boolean validateLoggedinUserByOtp(String jwt, String otp) {
		try {
			final ResponseEntity<Boolean> exchange = restTemplate.exchange(
					(userServiceUrl + "/api/user/common/username-otp-validation?otp=" + otp), HttpMethod.GET,
					buildHeaderEntity(jwt), Boolean.class);
			return exchange.getBody();
		} catch (Exception e) {
			throw new ConnectionException(extractMessage(e.getMessage()));
		}
	}

//	public UserInternalInfo getLoggedinAppUserInfo(String jwt) {
//		try {
//			final ResponseEntity<UserInternalInfo> exchange = restTemplate.exchange(
//					(userServiceUrl + "/api/user/app/auth/loggedin-app-user-info"), HttpMethod.GET,
//					buildHeaderEntity(jwt), UserInternalInfo.class);
//			return exchange.getBody();
//		} catch (Exception e) {
//			throw new ConnectionException(extractMessage(e.getMessage()));
//		}
//	}

//	public UserInternalInfo getLoggedinSystemUserInfo(String jwt) {
//		try {
//			final ResponseEntity<UserInternalInfo> exchange = restTemplate.exchange(
//					(userServiceUrl + "/api/user/system/auth/loggedin-system-user-info"), HttpMethod.GET,
//					buildHeaderEntity(jwt), UserInternalInfo.class);
//			return exchange.getBody();
//		} catch (Exception e) {
//			throw new ConnectionException(extractMessage(e.getMessage()));
//		}
//	}

	public boolean validateToken(String jwt) {
		try {
			final ResponseEntity<Boolean> isValidate = restTemplate.exchange(
					(userServiceUrl + "/api/user/common/validate-token"), HttpMethod.GET, buildHeaderEntity(jwt),
					Boolean.class);
			if (Boolean.TRUE.equals(isValidate.getBody())) {
				return Boolean.TRUE;
			} else {
				throw new UnauthorizedException("Unauthorized access");
			}
		} catch (Exception e) {
			throw new ConnectionException(extractMessage(e.getMessage()));
		}
	}

	private HttpEntity<String> buildHeaderEntity(String jwt) {
		final HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + jwt);
		return new HttpEntity<>(headers);
	}

	private String extractMessage(String message) {

		final String regex = "<error>(.*?)</error>";
		final Pattern pattern = Pattern.compile(regex);
		final Matcher matcher = pattern.matcher(message);
		String errorMessage = message;
		if (matcher.find()) {
			errorMessage = matcher.group(1);
		}
		
		final String[] split = errorMessage.split(":");
		return split[split.length - 1].replaceAll("[^a-zA-Z0-9 ]", "");
	}

}
