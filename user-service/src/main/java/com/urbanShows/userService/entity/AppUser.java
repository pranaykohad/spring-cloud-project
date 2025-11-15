package com.urbanShows.userService.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.urbanShows.userService.enums.Role;
import com.urbanShows.userService.enums.Status;

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
public class AppUser {

	@Id
	private String phone;

	private String internalPassword;

	private String displayName;

	private String email;

	private List<Role> roles;

	private String profilePicUrl;
	
	private Status status;
	
	private String otp;
	
	private LocalDateTime otpTimeStamp;

}
