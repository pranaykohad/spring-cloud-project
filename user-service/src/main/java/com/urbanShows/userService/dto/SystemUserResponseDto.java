package com.urbanShows.userService.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SystemUserResponseDto {

	@Email(message = "This email ia not valid")
	@NotNull(message = "Email cannot be null")
	private String email;

	@Pattern(regexp = "^[6-9]\\d{9}$", message = "Invalid phone number")
	private String phone;

	private String displayName;
	
	private String jwt;

}
