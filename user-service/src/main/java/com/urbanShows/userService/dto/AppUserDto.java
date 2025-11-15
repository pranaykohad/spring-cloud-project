package com.urbanShows.userService.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.urbanShows.userService.enums.Role;
import com.urbanShows.userService.enums.Status;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppUserDto {

	@NotNull(message = "Phone cannot be null")
	@Pattern(regexp = "^[6-9]\\d{9}$", message = "Invalid mobile number")
	private String phone;

	private String displayName;

	@Email(message = "This email ia not valid")
	private String email;

	private List<Role> roles;

	@JsonIgnore
	private String internalPassword;

	private String profilePicUrl;
	
	private Status status;
	
	@JsonIgnore
	private String otp;
	
	@JsonIgnore
	private LocalDateTime otpTimeStamp;
	
}
