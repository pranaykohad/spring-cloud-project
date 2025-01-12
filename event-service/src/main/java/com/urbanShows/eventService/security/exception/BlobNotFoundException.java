package com.urbanShows.eventService.security.exception;

public class BlobNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 6212924197746832775L;

	public BlobNotFoundException(String message) {
		super(message);
	}

}
