package com.urbanShows.userService.util;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.urbanShows.userService.entity.Role;
import com.urbanShows.userService.entity.RolePriority;
import com.urbanShows.userService.exception.UnauthorizedException;

public class RolesUtil {

	private RolesUtil() {
	}

	private static final Map<Role, Integer> ROLE_PRIORITY_MAP = Stream.of(RolePriority.values())
			.collect(Collectors.toMap(RolePriority::getRole, RolePriority::getPriority));

	public static boolean isHigherPriority(Role currentUserRole, Role targetUserRole) {
		if (getRolePriority(currentUserRole) <= getRolePriority(targetUserRole)) { // less is more priority
			return true;
		} else {
			throw new UnauthorizedException(
					"You are not authorized to perform this operation. Please contact your oadministrator for assistance");
		}
	}

	private static int getRolePriority(Role role) {
		return ROLE_PRIORITY_MAP.getOrDefault(role, 6);
	}

}
