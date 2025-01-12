package com.urbanShows.eventService.security.exception;

public class FileSizeExceedsException extends RuntimeException {

	private static final long serialVersionUID = 3615950074958953709L;
	
	public FileSizeExceedsException(String message) {
        super(message);
    }
}