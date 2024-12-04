package com.urbanShows.userService.exceptionHandler;

public class AccessDeniedException extends RuntimeException {

	private static final long serialVersionUID = -2616248295979851428L;

	private String message;

	public AccessDeniedException(String message) {
		super(message);
		this.message = message;
	}
}