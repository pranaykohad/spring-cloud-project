package com.urbanShows.userService.exceptionHandler;


public class UnauthorizedException extends RuntimeException {

	private static final long serialVersionUID = -8089939288226070763L;
	
	public UnauthorizedException(String message) {
        super(message);
    }
}