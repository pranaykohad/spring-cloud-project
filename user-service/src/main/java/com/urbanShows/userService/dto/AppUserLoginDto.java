package com.urbanShows.userService.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppUserLoginDto {

	@NotNull(message = "Phone cannot be null")
	@Pattern(regexp = "^[6-9]\\d{9}$", message = "Invalid mobile number")
	private String phone;

	@NotNull(message = "Otp cannot be null")
	private String otp;
}
