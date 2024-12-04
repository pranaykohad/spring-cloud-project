package com.urbanShows.userService.exceptionHandler;


public class FileSizeExceedsException extends RuntimeException {

	private static final long serialVersionUID = 3615950074958953709L;
	
	private String message;
	
	public FileSizeExceedsException(String message) {
        super(message);
        this.message = message;
    }
}