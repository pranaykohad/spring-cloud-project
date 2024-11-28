package com.urbanShows.userService.exceptionHandler;


public class UserAlreadyExistsException extends RuntimeException {

	private static final long serialVersionUID = -8089939288226070763L;
	
	public UserAlreadyExistsException(String message) {
        super(message);
    }
}