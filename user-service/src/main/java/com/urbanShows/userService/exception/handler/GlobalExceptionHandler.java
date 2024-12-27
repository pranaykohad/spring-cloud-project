package com.urbanShows.userService.exception.handler;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.urbanShows.userService.exception.AccessDeniedException;
import com.urbanShows.userService.exception.BlobNotFoundException;
import com.urbanShows.userService.exception.FileSizeExceedsException;
import com.urbanShows.userService.exception.GenericException;
import com.urbanShows.userService.exception.IncorrectOtpException;
import com.urbanShows.userService.exception.JwtParseException;
import com.urbanShows.userService.exception.UnauthorizedException;
import com.urbanShows.userService.exception.UserAlreadyExistsException;
import com.urbanShows.userService.exception.UserInactiveException;
import com.urbanShows.userService.exception.UserNotFoundException;

import io.jsonwebtoken.ExpiredJwtException;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(UserNotFoundException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public ResponseEntity<Map<String, String>> handleUserNotFoundException(UserNotFoundException ex) {
		return new ResponseEntity<>(buildErrorMap(ex), HttpStatus.FORBIDDEN);
	}
	
	@ExceptionHandler(AuthorizationDeniedException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public ResponseEntity<Map<String, String>> handleAuthorizationDeniedException(AuthorizationDeniedException ex) {
		return new ResponseEntity<>(buildErrorMap(ex), HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler(UnauthorizedException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ResponseEntity<Map<String, String>> handleUnauthorizedException(UnauthorizedException ex) {
		return new ResponseEntity<>(buildErrorMap(ex), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(UserInactiveException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ResponseEntity<Map<String, String>> handleUserInactiveException(UserInactiveException ex) {
		return new ResponseEntity<>(buildErrorMap(ex), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(JwtParseException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ResponseEntity<Map<String, String>> handleJwtParseException(JwtParseException ex) {
		return new ResponseEntity<>(buildErrorMap(ex), HttpStatus.INTERNAL_SERVER_ERROR);
	}	
	
	@ExceptionHandler(IncorrectOtpException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ResponseEntity<Map<String, String>> handleIncorrectOtpException(IncorrectOtpException ex) {
		return new ResponseEntity<>(buildErrorMap(ex), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(BlobNotFoundException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ResponseEntity<Map<String, String>> blobNotFoundException(BlobNotFoundException ex) {
		return new ResponseEntity<>(buildErrorMap(ex), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(GenericException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ResponseEntity<Map<String, String>> handleGenericException(GenericException ex) {
		return new ResponseEntity<>(buildErrorMap(ex), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(AccessDeniedException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public ResponseEntity<Map<String, String>> handleAccessDeniedException(AccessDeniedException ex) {
		return new ResponseEntity<>(buildErrorMap(ex), HttpStatus.UNAUTHORIZED);
	}
	
	@ExceptionHandler(ExpiredJwtException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public ResponseEntity<Map<String, String>> handleExpiredJwtException(ExpiredJwtException ex) {
		return new ResponseEntity<>(buildErrorMap(ex), HttpStatus.UNAUTHORIZED);
	}


	@ExceptionHandler(UserAlreadyExistsException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	public ResponseEntity<Map<String, String>> handleUserAleadyExistsException(UserAlreadyExistsException ex) {
		return new ResponseEntity<>(buildErrorMap(ex), HttpStatus.CONFLICT);
	}
	

	@ExceptionHandler(FileSizeExceedsException.class)
	@ResponseStatus(HttpStatus.PRECONDITION_FAILED)
	public ResponseEntity<Map<String, String>> handleFileSizeExceedsException(FileSizeExceedsException ex) {
		return new ResponseEntity<>(buildErrorMap(ex), HttpStatus.PRECONDITION_FAILED);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach(error -> {
			String fieldName = ((org.springframework.validation.FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});
		return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
	}
	
	private HashMap<String, String> buildErrorMap(Exception ex) {
		final HashMap<String, String> map = new HashMap<>();
		map.put("error", ex.getMessage());
		return map;
	}
}
