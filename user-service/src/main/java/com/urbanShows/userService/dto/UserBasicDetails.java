package com.urbanShows.userService.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserBasicDetails {
	
	private String userName;

	private String displayName;
	
	private String profilePicUrl;

	private MultipartFile profilePicFile;

}
