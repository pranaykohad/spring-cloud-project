package com.urbanShows.userService.constants;

import java.util.ArrayList;
import java.util.List;

public class ComonConstants {

	private ComonConstants() {
	}

	public static final List<String> VALID_IMAGE_FORMAT = new ArrayList<>(
			List.of("jpg", "jpeg", "png", "heic", "webp"));
	
	public static final int PAGE_SIZE = 20;
	
	public static final long MAX_IMAGE_SIZE = 5242880; // 5 MB = 5242880

}
