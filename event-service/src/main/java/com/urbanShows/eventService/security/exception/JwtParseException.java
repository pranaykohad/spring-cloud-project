package com.urbanShows.eventService.security.exception;

public class JwtParseException extends RuntimeException {

	private static final long serialVersionUID = 3979044013177605377L;
	
	public JwtParseException(String message) {
		super(message);
	}

}
