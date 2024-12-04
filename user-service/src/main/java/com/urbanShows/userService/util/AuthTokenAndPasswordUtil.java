package com.urbanShows.userService.util;

import java.security.SecureRandom;

public class AuthTokenAndPasswordUtil {

	private AuthTokenAndPasswordUtil() {

	}

	public static String generateAuthToken() {
		return String.valueOf(100000 + new SecureRandom().nextInt(900000));
	}

	public static String generatorPassword() {
		final String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*";
		final SecureRandom random = new SecureRandom();
		final StringBuilder result = new StringBuilder(20);
		for (int i = 0; i < 20; i++) {
			final int index = random.nextInt(characters.length());
			result.append(characters.charAt(index));
		}
		return result.toString();
	}

}
