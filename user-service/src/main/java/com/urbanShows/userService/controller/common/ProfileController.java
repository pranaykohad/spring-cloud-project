package com.urbanShows.userService.controller.common;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.urbanShows.userService.aws.AwsS3Service;
//import com.urbanShows.userService.azure.AzureBlobStorageService;
import com.urbanShows.userService.dto.AttachmentDto;
import com.urbanShows.userService.exception.UserNotFoundException;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("api/user/profile")
@AllArgsConstructor
public class ProfileController {
	
	private final AwsS3Service awsS3Service;
//	private final AzureBlobStorageService azureBlobStorageService;

//	@GetMapping("download")
//	public ResponseEntity<byte[]> downloadFile() {
//		byte[] fileData = azureBlobStorageService.downloadFile("screenshot.jpg");
//		HttpHeaders headers = new HttpHeaders();
//		headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=screenshot.jpg");
//		headers.setContentType(MediaType.IMAGE_PNG);
//		return new ResponseEntity<>(fileData, headers, HttpStatus.OK);
//	}

//	@GetMapping("download")
//	public ResponseEntity<AttachmentDto> downloadFile() {
//		final byte[] fileData = azureBlobStorageService.downloadFile("screenshot.jpg");
//		final AttachmentDto attachment = new AttachmentDto();
//		attachment.setFileContent(fileData);
//		attachment.setFilename("screenshot.jpg");
//		attachment.setMediaType(MediaType.IMAGE_PNG);
//		return new ResponseEntity<>(attachment, HttpStatus.OK);
//	}

//	@PatchMapping("update-profile-pic")
//	public ResponseEntity<String> uploadAppUserProfilePic(@RequestParam MultipartFile file,
//			@RequestPart(required = false) String phoneNumber, @RequestPart(required = false) String userName,
//			@RequestPart String otp, @RequestPart String role) {
//		String url = "";
//		if (phoneNumber != null && !phoneNumber.isEmpty()) {
//			url = azureBlobStorageService.uploadAppUserProfile(file, phoneNumber, otp);
//		} else if (userName != null && !userName.isEmpty()) {
//			url = azureBlobStorageService.uploadSystemUserProfile(file, userName, otp);
//		} else {
//			throw new UserNotFoundException("User doesnot exists in the system");
//		}
//		return new ResponseEntity<>(url, HttpStatus.OK);
//	}
	
	
	
	@GetMapping("download/{fileName}")
	public ResponseEntity<byte[]> downloadFile(@PathVariable String fileName) {
		byte[] data = awsS3Service.downloadFile(fileName);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(data);
	}
	
	@DeleteMapping("delete/{fileName}")
	public ResponseEntity<String> deleteFile(@PathVariable String fileName) {
		final String response = awsS3Service.deleteFile(fileName);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(response);
	}
	
//	@GetMapping("download")
//	public ResponseEntity<byte[]> downloadFile() {
//		byte[] fileData = awsS3Service.downloadFile("screenshot.jpg");
//		HttpHeaders headers = new HttpHeaders();
//		headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=screenshot.jpg");
//		headers.setContentType(MediaType.IMAGE_PNG);
//		return new ResponseEntity<>(fileData, headers, HttpStatus.OK);
//	}

}
