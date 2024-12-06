package com.urbanShows.userService.exceptionHandler;

public class BlobNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 6212924197746832775L;

	private String message;

	public BlobNotFoundException(String message) {
		super(message);
		this.message = message;
	}

}
