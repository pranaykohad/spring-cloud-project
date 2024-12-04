package com.urbanShows.userService.exceptionHandler;

public class UserNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -3196325847158105507L;

	private String message;

	public UserNotFoundException(String message) {
		super(message);
		this.message = message;
	}
}