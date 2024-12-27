package com.urbanShows.userService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserActivationDto {

	private String device;

	private String contactId;

	private String otp;

}
