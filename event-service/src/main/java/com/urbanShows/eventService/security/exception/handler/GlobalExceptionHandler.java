package com.urbanShows.eventService.security.exception.handler;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.urbanShows.eventService.security.exception.ConnectionException;
import com.urbanShows.eventService.security.exception.JwtParseException;
import com.urbanShows.eventService.security.exception.ServiceUnavailableException;

import io.jsonwebtoken.security.SignatureException;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ServiceUnavailableException.class)
	@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
	public ResponseEntity<Map<String, String>> handleServiceUnavailableException(ServiceUnavailableException ex) {
		return new ResponseEntity<>(buildErrorMap(ex), HttpStatus.SERVICE_UNAVAILABLE);
	}

	@ExceptionHandler(AuthorizationDeniedException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public ResponseEntity<Map<String, String>> handleAuthorizationDeniedException(AuthorizationDeniedException ex) {
		return new ResponseEntity<>(buildErrorMap(ex), HttpStatus.FORBIDDEN);
	}
	
	@ExceptionHandler(SignatureException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public ResponseEntity<Map<String, String>> handleSignatureException(SignatureException ex) {
		return new ResponseEntity<>(buildErrorMap(ex), HttpStatus.FORBIDDEN);
	}
	
	@ExceptionHandler(ConnectionException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public ResponseEntity<Map<String, String>> handleConnectionException(ConnectionException ex) {
		return new ResponseEntity<>(buildErrorMap(ex), HttpStatus.INTERNAL_SERVER_ERROR);
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
