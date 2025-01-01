package com.urbanShows.eventService.security.exception;

public class UserInactiveException extends RuntimeException {

	private static final long serialVersionUID = 2361865900894472034L;

	public UserInactiveException(String message) {
		super(message);
	}
}