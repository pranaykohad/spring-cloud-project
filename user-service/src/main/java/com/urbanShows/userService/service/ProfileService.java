package com.urbanShows.userService.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.urbanShows.userService.aws.AwsS3Service;
import com.urbanShows.userService.repository.JwtTokenRepo;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProfileService {
	
	@Value("${aws.s3.folder-name.profile}")
	private String profileFolderName;
	
	private final AwsS3Service awsS3Service;
	
	public String updateProfilePic(MultipartFile file, String phoneNumber, String targetUserName) {
		String url = null;
		if(StringUtils.hasText(phoneNumber) && !StringUtils.hasText(targetUserName)) {
			// Update profile pic using phone number for APP user
		}
		if(!StringUtils.hasText(phoneNumber) && StringUtils.hasText(targetUserName)) {
			// Update profile pic using targetUserName for System user
			url = awsS3Service.uploadFile(file, profileFolderName, targetUserName);
			log.info("Profile picture uploaded successfully. URL: {}", url);
		}
		return url;
	}

}
