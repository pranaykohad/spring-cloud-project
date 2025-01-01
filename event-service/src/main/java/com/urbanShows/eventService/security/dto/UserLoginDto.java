package com.urbanShows.eventService.security.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginDto {

	@NotNull(message = "Username cannot be null")
	private String userName;

	@NotNull(message = "Password cannot be null")
	private String password;
}
