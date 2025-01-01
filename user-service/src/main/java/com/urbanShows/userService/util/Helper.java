package com.urbanShows.userService.util;

import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import com.urbanShows.userService.azure.AzureConfig;
import com.urbanShows.userService.enums.Role;
import com.urbanShows.userService.exception.FileSizeExceedsException;
import com.urbanShows.userService.exception.InvalidFileFormatException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;

public class Helper {

	private static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";
	
	private Helper() {
	}
	
	public static Pair<String, String> extractUserNameAndToken(HttpServletRequest request) {
		String authHeader = request.getHeader("Authorization");
		String token = null;
		String id = null;
		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			token = authHeader.substring(7);
			id = Helper.extractUsername(token);
		}
		return Pair.of(id, token); 
	}
	
	public static String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public static Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}
	
	public static Key getSignKey() {
		byte[] keyBytes = Decoders.BASE64.decode(SECRET);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	public static UserDetails buildUser(String userName, String password, List<Role> roles) {
		final List<String> list = new ArrayList<>();
		roles.forEach(i -> list.add(i.name()));
		return User.builder().username(userName).password(password).roles(list.toArray(new String[0])).build();
	}
	
	private static <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}
	
	private static Claims extractAllClaims(String token) {
		return Jwts.parser().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
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
