package com.urbanShows.userService.dto;

import java.util.List;

import com.urbanShows.userService.entity.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {
	
	@NotNull(message = "UseName cannot be null")
	private String userName;

	@Email(message = "This email is not valid")
	@NotNull(message = "Email cannot be null")
	private String email;

	@Pattern(regexp = "^[6-9]\\d{9}$", message = "Invalid phone number")
	private String phone;

	private String displayName;
	
	private String jwt;
	
	private String profilePicUrl;
	
	@NotEmpty(message = "Roles cannot be empty")
	private List<Role> roles;

}
