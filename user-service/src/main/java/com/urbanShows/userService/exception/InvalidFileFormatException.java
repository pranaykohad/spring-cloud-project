package com.urbanShows.userService.exception;

public class InvalidFileFormatException extends RuntimeException {

	private static final long serialVersionUID = -1998601405739608400L;
//"Invalid file format. Allowed format list : [" + AzureConfig.VALID_IMAGE_FORMAT + "]"
	public InvalidFileFormatException(String message) {
		super(message);
	}
}