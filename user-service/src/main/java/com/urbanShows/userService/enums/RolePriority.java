package com.urbanShows.userService.enums;

public enum RolePriority {

	SUPER_ADMIN_USER_PRIORITY(1, Role.SUPER_ADMIN_USER), 
	SYSTEM_USER_PRIORITY(2, Role.SYSTEM_USER),
	ORGANIZER_USER_PRIORITY(3, Role.ORGANIZER_USER), 
	APP_USER_PRIORITY(3, Role.APP_USER);

	private final int priority;
	private final Role role;

	RolePriority(int priority, Role role) {
		this.priority = priority;
		this.role = role;
	}

	public Role getRole() {
		return role;
	}

	public int getPriority() {
		return priority;
	}

}