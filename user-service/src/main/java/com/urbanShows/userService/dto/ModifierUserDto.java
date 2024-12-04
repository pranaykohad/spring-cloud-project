package com.urbanShows.userService.dto;

import java.util.List;

import com.urbanShows.userService.entity.Role;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModifierUserDto {

	private String phone;

	private String userName;

	@NotEmpty(message = "Roles canonot be empty")
	private List<Role> roles;

	@NotNull(message = "Otp cannot be null")
	private String otp;

}
