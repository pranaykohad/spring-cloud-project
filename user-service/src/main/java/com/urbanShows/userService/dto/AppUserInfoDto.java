package com.urbanShows.userService.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.urbanShows.userService.entity.Role;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppUserInfoDto {

	@NotNull(message = "Phone cannot be null")
	@Pattern(regexp = "^[6-9]\\d{9}$", message = "Invalid mobile number")
	private String phone;

	private String otp;
	
	private String displayName;

	private String email;
	
	private List<Role> roles;
	
	@JsonIgnore
	private String internalPassword;

}
