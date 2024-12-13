package com.urbanShows.userService.exceptionHandler;

public class JwtParseException extends RuntimeException {

	private static final long serialVersionUID = 3979044013177605377L;
	
	private String message;

	public JwtParseException(String message) {
		super(message);
		this.message = message;
	}

}
