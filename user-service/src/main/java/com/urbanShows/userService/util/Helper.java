package com.urbanShows.userService.util;

public class Helper {

	public static boolean isPhonenumber(String phone) {
		return phone.length() == 10 && phone.chars().allMatch(Character::isDigit);
	}

}
