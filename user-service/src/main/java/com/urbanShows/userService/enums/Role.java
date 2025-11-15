package com.urbanShows.userService.enums;

public enum Role {
	// SUPER_ADMIN_USER: Default active user with all privileges, can approve all
	// other users (only 1 user in the system)
	// SYSTEM_USER: User who can manage organizers and app users and approve their
	// access (multiple users in the system)
	// ORGANIZER_USER: User who can organize events, sell tickets and manage their
	// events (multiple users in the system)
	// APP_USER: Regular user who can buy tickets and attend events (multiple users
	// in the system)
	SUPER_ADMIN_USER, SYSTEM_USER, ORGANIZER_USER, APP_USER
}
