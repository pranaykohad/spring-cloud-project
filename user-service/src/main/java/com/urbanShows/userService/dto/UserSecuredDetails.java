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
public class UserSecuredDetails {

	@NotNull(message = "Username cannot be null")
	private String userName;

	private String password;

	private String otp;

	@Pattern(regexp = "^[6-9]\\d{9}$", message = "Invalid mobile number")
	private String phone;

	@Email(message = "This email is not valid")
	private String email;

}
