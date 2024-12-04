package com.urbanShows.userService.exceptionHandler;

public class InvalidFileFormatException extends RuntimeException {

	private static final long serialVersionUID = -1998601405739608400L;
//"Invalid file format. Allowed format list : [" + AzureConfig.VALID_IMAGE_FORMAT + "]"
	private String message;

	public InvalidFileFormatException(String message) {
		super(message);
		this.message = message;
	}
}