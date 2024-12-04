package com.urbanShows.userService.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.urbanShows.userService.entity.Role;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//public class AuthTokenDto {
//
//	@Id
//	@NotNull(message = "Phone Number cannot be null")
//	@Pattern(regexp = "^[6-9]\\d{9}$", message = "Invalid Phone number")
//	private String phoneNumber;
//
//	@NotNull(message = "Auth Token cannot be null")
//	private String authToken;
//	
//	private LocalDateTime tokenTimeStamp;
//	
//	@NotNull(message = "Roles cannot be null")
//	private List<Role> roles; 
//}
