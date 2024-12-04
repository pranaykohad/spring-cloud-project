package com.urbanShows.userService.dto;

import java.util.List;

import com.urbanShows.userService.entity.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppUserSigninReqDto {

	@NotNull(message = "Phone cannot be null")
	@Pattern(regexp = "^[6-9]\\d{9}$", message = "Invalid mobile number")
	private String phone;

	private String displayName;

	@Email(message = "This email ia not valid")
	private String email;

	private List<Role> roles;

}
