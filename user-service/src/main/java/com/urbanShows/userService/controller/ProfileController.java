package com.urbanShows.userService.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.urbanShows.userService.azure.AzureBlobStorageService;
import com.urbanShows.userService.service.AppUserService;
import com.urbanShows.userService.service.SystemUserService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("api/user/profile")
@AllArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class ProfileController {

	private AzureBlobStorageService azureBlobStorageService;

	private AppUserService appUserService;

	private SystemUserService systemUserService;

	@GetMapping("download")
	public ResponseEntity<byte[]> downloadFile() {
		byte[] fileData = azureBlobStorageService.downloadFile("prerna passport photo.jpg");
		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + "prerna passport photo.jpg" + ".png");
		headers.setContentType(MediaType.IMAGE_PNG);
		return new ResponseEntity<>(fileData, headers, HttpStatus.OK);
	}

//	@PostMapping("upload")
//	public ResponseEntity<String> uploadAppUserProfilePic(@RequestParam MultipartFile file,
//			@RequestPart String phoneNumber, @RequestPart(required = false) String userName, @RequestPart String otp) {
//		String url = null;
//		if (phoneNumber != null) { 
//			AppUserInfoDto appUserInfoDto = new AppUserInfoDto();
//			appUserInfoDto.setPhone(phoneNumber);
//			appUserInfoDto.setOtp(otp);
//			appUserService.verifyOtp(appUserInfoDto);
//			url = azureBlobStorageService.uploadAppUserProfile(file, phoneNumber);
//		} else {
//			SystemUserInfoDto systemUserInfoDto = new SystemUserInfoDto();
//			systemUserInfoDto.setPhone(phoneNumber);
//			systemUserInfoDto.setOtp(otp);
//			systemUserService.verifyOtp(systemUserInfoDto);
//			url = azureBlobStorageService.uploadSystemUserProfile(file, userName);
//		}
//		return new ResponseEntity<>(url, HttpStatus.OK);
//	}

}
