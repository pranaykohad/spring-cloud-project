package com.urbanShows.eventService.security.exception;

public class ServiceUnavailableException extends RuntimeException {

	private static final long serialVersionUID = 9009041118025789875L;

	public ServiceUnavailableException(String message) {
		super(message);
	}
	
}
