package com.urbanShows.userService.exception;


public class UserAlreadyExistsException extends RuntimeException {

	private static final long serialVersionUID = -1121428592829991726L;
	
	public UserAlreadyExistsException(String message) {
        super(message);
    }
}