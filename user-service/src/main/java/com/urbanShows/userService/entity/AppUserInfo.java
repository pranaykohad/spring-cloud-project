package com.urbanShows.userService.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppUserInfo {

	@Id
	private String phone;
	
	private String internalPassword;
	
	private String displayName;

	private String otp;

	private String email;

	private List<Role> roles;
	
	private LocalDateTime otpDateTime; 

}
