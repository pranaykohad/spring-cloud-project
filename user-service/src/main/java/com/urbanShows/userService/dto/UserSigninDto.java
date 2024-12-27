package com.urbanShows.userService.dto;

import java.util.List;

import com.urbanShows.userService.enums.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSigninDto {

	@NotNull(message = "Username cannot be null")
	private String userName;

	@NotNull(message = "Password cannot be null")
	// disable for simplicity, just for testing
//	@Size(min = 8, message = "Password must be at least 8 characters long")
//	@Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$", message = "Password must contain at least one digit, one lowercase letter, one uppercase letter, and one special character (@#$%^&+=)")
	private String password;

	@Email(message = "This email is not valid")
	private String email;

	@Pattern(regexp = "^[6-9]\\d{9}$", message = "Invalid phone number")
	@Size(min = 10, max = 10, message = "Invalid mobile number")
	private String phone;

	@NotNull(message = "Display name cannot be null")
	@Size(min = 3, message = "Display name must be at least 3 characters long")
	private String displayName;
	
	@NotEmpty(message = "Roles cannot be empty")
	private List<Role> roles;

}
