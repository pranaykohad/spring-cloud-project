package com.urbanShows.eventService.security.util;

//import com.urbanShows.eventService.azure.AzureConfig;

public class Helper {

	private Helper() {
	}

	public static String getFileNameWithoutTimeAndExtension(String fileName) {
		final int dotIndex = fileName.lastIndexOf('.');
		String newFileName = "";
		newFileName = dotIndex > 0 ? fileName.substring(0, dotIndex) : fileName;
		final int dashIndex = newFileName.lastIndexOf('-');
		return dashIndex > 0 ? fileName.substring(0, dashIndex + 1) : fileName;
	}

//	public static void validateBlob(String originalFileName, long fileSize) {
//		if (!isValidFormat(originalFileName)) {
//			throw new InvalidFileFormatException("File format is not correct: " + originalFileName);
//		}
//		if (!isFileSizeExceeds(fileSize)) {
//			throw new FileSizeExceedsException("File size exceeds: " + fileSize);
//		}
//	}

//	public static List<Role> getLoggedinUserRoles(Principal principal) {
//		if (principal instanceof Authentication authentication) {
//			return authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
//					.map(Helper::fromAuthority).toList();
//		}
//		return List.of(); // No roles present
//	}

	public static boolean isPhonenumber(String phone) {
		return phone.length() == 10 && phone.chars().allMatch(Character::isDigit);
	}

//	public List<String> getAuthoritiesFromToken(String token) {
//		Claims claims = Jwts.parser().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
//		return claims.get("roles", List.class);
//	}

//	private static boolean isValidFormat(String fileName) {
//		final String fileExtension = getFileExtension(fileName);
//		return fileExtension != null && AzureConfig.VALID_IMAGE_FORMAT.contains(fileExtension.toLowerCase());
//	}

	private static String getFileExtension(String fileName) {
		int lastDotIndex = fileName.lastIndexOf('.');
		return (lastDotIndex != -1 && lastDotIndex < fileName.length() - 1) ? fileName.substring(lastDotIndex + 1)
				: null;
	}

//	private static boolean isFileSizeExceeds(long size) {
//		return size < AzureConfig.MAX_IMAGE_SIZE;
//	}

}
