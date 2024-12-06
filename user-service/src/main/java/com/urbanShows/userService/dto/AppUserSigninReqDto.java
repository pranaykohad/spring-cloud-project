package com.urbanShows.userService.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppUserSigninReqDto {

	@NotNull(message = "Phone cannot be null")
	@Pattern(regexp = "^[6-9]\\d{9}$", message = "Invalid mobile number")
	@Size(min = 10, max = 10, message = "Invalid mobile number")
	private String phone;

	@NotNull(message = "Display name cannot be null")
	@Size(min = 3, message = "Display name must be at least 3 characters long")
	private String displayName;

	@Email(message = "This email is not valid")
	private String email;

}
