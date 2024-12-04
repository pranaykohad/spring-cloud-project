package com.urbanShows.userService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateDto {
	
	private ModifierUserDto modifierUserDto;

	private TargetUserDto targetUserDto;
}
