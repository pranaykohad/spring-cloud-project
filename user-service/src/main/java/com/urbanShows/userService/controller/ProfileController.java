package com.urbanShows.userService.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.urbanShows.userService.azure.AzureBlobStorageService;
import com.urbanShows.userService.exceptionHandler.AccessDeniedException;
import com.urbanShows.userService.exceptionHandler.UserNotFoundException;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("api/user/profile")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class ProfileController {

	private final AzureBlobStorageService azureBlobStorageService;

	@GetMapping("download")
	public ResponseEntity<byte[]> downloadFile() {
		byte[] fileData = azureBlobStorageService.downloadFile("prerna passport photo.jpg");
		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + "prerna passport photo.jpg" + ".png");
		headers.setContentType(MediaType.IMAGE_PNG);
		return new ResponseEntity<>(fileData, headers, HttpStatus.OK);
	}

	@PatchMapping("update-profile-pic")
	public ResponseEntity<String> uploadAppUserProfilePic(@RequestParam MultipartFile file,
			@RequestPart(required = false) String phoneNumber, @RequestPart(required = false) String userName,
			@RequestPart String otp, @RequestPart String role) {
		String url = "";
		if (phoneNumber != null && !phoneNumber.isEmpty()) {
			url = azureBlobStorageService.uploadAppUserProfile(file, phoneNumber, otp);
		} else if (userName != null && !userName.isEmpty()) {
			url = azureBlobStorageService.uploadSystemUserProfile(file, userName, otp);
		} else {
			throw new UserNotFoundException("User doesnot exists in the system");
		}
		return new ResponseEntity<>(url, HttpStatus.OK);
	}

}
