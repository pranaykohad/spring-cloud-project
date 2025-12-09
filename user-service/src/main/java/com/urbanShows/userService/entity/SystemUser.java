package com.urbanShows.userService.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.urbanShows.userService.enums.Role;
import com.urbanShows.userService.enums.Status;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemUser {

	@Id
	private String userName;

	private String password;

	private String email;

	private String displayName;

	private List<Role> roles;

	private String phone;

	@JsonIgnore
	private String profilePicUrl;
	
	@Transient
	private Object profilePic;

	private String otp;

	private LocalDateTime otpTimeStamp;

	private LocalDateTime createdAt;

	@Enumerated(EnumType.STRING)
	private Status status;

	private boolean phoneValidated;

	private boolean emailValidated;

}
