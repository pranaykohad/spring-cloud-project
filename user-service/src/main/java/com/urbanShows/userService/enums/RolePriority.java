package com.urbanShows.userService.enums;

public enum RolePriority {

	SUPER_ADMIN_USER_PRIORITY(1, Role.SUPER_ADMIN_USER),
	ADMIN_USER_PRIORITY(2, Role.ADMIN_USER),
	SYSTEM_USER_PRIORITY(3, Role.SYSTEM_USER),
	SUPPORT_USER_PRIORITY(4, Role.SUPPORT_USER), 
	ORGANIZER_USER_PRIORITY(5, Role.ORGANIZER_USER),
	APP_USER_PRIORITY(5, Role.APP_USER),
	SECURITY(6, Role.SECURITY);
	
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