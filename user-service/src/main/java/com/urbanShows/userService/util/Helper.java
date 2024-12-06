package com.urbanShows.userService.util;

import com.urbanShows.userService.azure.AzureConfig;
import com.urbanShows.userService.exceptionHandler.FileSizeExceedsException;
import com.urbanShows.userService.exceptionHandler.InvalidFileFormatException;

public class Helper {

	private Helper() {
	}

	public static boolean isPhonenumber(String phone) {
		return phone.length() == 10 && phone.chars().allMatch(Character::isDigit);
	}
	
	public static void validateBlob(String originalFileName, long fileSize) {
		if (!isValidFormat(originalFileName)) {
			throw new InvalidFileFormatException("File format is not correct: " + originalFileName);
		}
		if (!isFileSizeExceeds(fileSize)) {
			throw new FileSizeExceedsException("File size exceeds: " + fileSize);
		}
	}
	
	private static boolean isFileSizeExceeds(long size) {
		return size < AzureConfig.MAX_IMAGE_SIZE;
	}

	private static boolean isValidFormat(String fileName) {
		final String fileExtension = getFileExtension(fileName);
		return fileExtension != null && AzureConfig.VALID_IMAGE_FORMAT.contains(fileExtension.toLowerCase());
	}

	private static String getFileExtension(String fileName) {
		int lastDotIndex = fileName.lastIndexOf('.');
		return (lastDotIndex != -1 && lastDotIndex < fileName.length() - 1) ? fileName.substring(lastDotIndex + 1)
				: null;
	}

}
