package com.urbanShows.userService.exceptionHandler;


public class UserAlreadyExistsException extends RuntimeException {

	private static final long serialVersionUID = -1121428592829991726L;
	
	private String message;

	public UserAlreadyExistsException(String message) {
        super(message);
        this.message = message;
    }
}