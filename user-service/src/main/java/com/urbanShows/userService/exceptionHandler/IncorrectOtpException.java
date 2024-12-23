package com.urbanShows.userService.exceptionHandler;

public class IncorrectOtpException extends RuntimeException {

	private static final long serialVersionUID = 3034387251891425271L;
	
	private String message;

	public IncorrectOtpException(String message) {
		super(message);
		this.message = message;
	}
}