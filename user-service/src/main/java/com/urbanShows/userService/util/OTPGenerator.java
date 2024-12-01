package com.urbanShows.userService.util;

import java.security.SecureRandom;

public class OTPGenerator {

	private OTPGenerator() {

	}

	public static String generateOTP() {
		int otp = 1000 + new SecureRandom().nextInt(9000);
		return String.valueOf(otp);
	}

	public static String generatorPassword() {
		String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*";
		SecureRandom random = new SecureRandom();
		StringBuilder result = new StringBuilder(20);
		for (int i = 0; i < 20; i++) {
			int index = random.nextInt(characters.length());
			result.append(characters.charAt(index));
		}
		return result.toString();
	}

}
