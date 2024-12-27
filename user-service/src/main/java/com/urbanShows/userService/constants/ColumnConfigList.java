package com.urbanShows.userService.constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ColumnConfigList {

	private ColumnConfigList() {
	}

	public static final List<String> USER_COlUMNS = new ArrayList<>(Arrays.asList("userName", "displayName", "password",
			"phone", "email", "roles", "profilePicUrl", "status", "createdAt"));

}
