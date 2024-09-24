package com.urbanShows.customerService.exceptionHandler;

public class UserNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -3196325847158105507L;

	public UserNotFoundException(String message) {
        super(message);
    }
}