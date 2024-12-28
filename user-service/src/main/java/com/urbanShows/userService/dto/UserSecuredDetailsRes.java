package com.urbanShows.userService.dto;


import com.urbanShows.userService.enums.Status;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSecuredDetailsRes {

	@NotNull(message = "Username cannot be null")
	private String userName;

	private String password;

	@Pattern(regexp = "^[6-9]\\d{9}$", message = "Invalid mobile number")
	private String phone;

	@Email(message = "This email is not valid")
	private String email;
	
	private Status status;
	
	private boolean phoneValidated;
	
	private boolean emailValidated;

}
