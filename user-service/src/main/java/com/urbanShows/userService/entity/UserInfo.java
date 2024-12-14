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
public class UserInfo {

	@Id
	private String userName;

	private String password;

	private String email;
	
	private String displayName;

	private List<Role> roles;

	private String phone;
	
	private String profilePicUrl;
	
	private String otp;
	
	private LocalDateTime otpTimeStamp;
	
}
