package com.urbanShows.userService.dto;

import java.util.List;

import com.urbanShows.userService.entity.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoDto {

	@NotNull(message = "Name cannot be null")
	private String name;

	@Email(message = "This email ia not valid")
	@NotNull(message = "Email cannot be null")
	private String email;

	private String password;

	private List<Role> roles;

}
