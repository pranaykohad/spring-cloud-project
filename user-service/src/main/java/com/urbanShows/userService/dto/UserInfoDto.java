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
public class UserInfoDto {

	@NotNull(message = "Username cannot be null")
	private String userName;

	@NotNull(message = "Password cannot be null")
	private String password;

	@Email(message = "This email ia not valid")
	@NotNull(message = "Email cannot be null")
	private String email;

	@Pattern(regexp = "^[6-9]\\d{9}$", message = "Invalid phone number")
	private String phone;
	
	private String displayName;

	private List<Role> roles;
	
	private String profilePicUrl;
	
	private LocalDateTime createdAt;
	
	private Status status;
	
	private boolean isPhoneValidated;
	
	private boolean isEmailValidated;
	
	@JsonIgnore
	private String otp;
	
	@JsonIgnore
	private LocalDateTime otpTimeStamp;
	
}
