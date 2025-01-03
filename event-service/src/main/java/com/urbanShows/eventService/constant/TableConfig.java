package com.urbanShows.eventService.constant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TableConfig {

	private TableConfig() {
	}

	public static final List<String> EVENT_COlUMNS = new ArrayList<>(Arrays.asList("id", "eventName", "createdOn",
			"bookingOpenAt", "bookingCloseAt", "bookingOpen", "userMinAge", "organizer"));
	
	public static final int PAGE_SIZE = 20;
	
}
