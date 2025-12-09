package com.urbanShows.userService.controller.common;

import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.urbanShows.userService.entity.SystemUser;
import com.urbanShows.userService.service.ProfileService;
import com.urbanShows.userService.service.SystemUserService;
import com.urbanShows.userService.util.RolesUtil;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("api/user/profile")
@AllArgsConstructor
public class ProfileController {

	private final SystemUserService systemUserService;
	private final ProfileService profileService;

//	@Value("${aws.s3.folder-name.profile}")
//	private String profileFolderName;

//	private final AwsS3Service awsS3Service;
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

//	@PatchMapping("profile-pic")
//	public ResponseEntity<Boolean> uploadProfilePic(@RequestParam MultipartFile file,
//			@RequestPart(required = false) String phoneNumber, @RequestPart(required = false) String tergetUserName,
//			Principal principal) {
//		final SystemUser loggedInUser = systemUserService.getActiveExistingSystemUser(principal.getName());
//		final SystemUser targetUser = systemUserService.getExistingSystemUser(tergetUserName);
//		RolesUtil.isHigherPriority(loggedInUser.getRoles().get(0), targetUser.getRoles().get(0));
//		Boolean isProfileUpdated = profileService.updateProfilePic(file, phoneNumber, tergetUserName);
//		return ResponseEntity.ok(isProfileUpdated);

//		String url = "";
//		if (phoneNumber != null && !phoneNumber.isEmpty()) {
//			url = azureBlobStorageService.uploadAppUserProfile(file, phoneNumber, otp);
//		} else if (tergetUserName != null && !tergetUserName.isEmpty()) {
//			url = azureBlobStorageService.uploadSystemUserProfile(file, tergetUserName, otp);
//		} else {
//			throw new UserNotFoundException("User doesnot exists in the system");
//		}
//		return new Respons eEntity<>(url, HttpStatus.OK);
//	}

//	@GetMapping("download/{fileName}")
//	public ResponseEntity<byte[]> downloadFile(@PathVariable String fileName) {
//		byte[] data = awsS3Service.downloadFile(fileName, profileFolderName);
//        return ResponseEntity.ok()
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
//                .contentType(MediaType.APPLICATION_OCTET_STREAM)
//                .body(data);
//	}
//	
//	@DeleteMapping("delete/{fileName}")
//	public ResponseEntity<String> deleteFile(@PathVariable String fileName) {
//		final String response = awsS3Service.deleteFile(fileName, profileFolderName);
//        return ResponseEntity.ok()
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
//                .contentType(MediaType.APPLICATION_OCTET_STREAM)
//                .body(response);
//	}

//	@GetMapping("download")
//	public ResponseEntity<byte[]> downloadFile() {
//		byte[] fileData = awsS3Service.downloadFile("screenshot.jpg");
//		HttpHeaders headers = new HttpHeaders();
//		headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=screenshot.jpg");
//		headers.setContentType(MediaType.IMAGE_PNG);
//		return new ResponseEntity<>(fileData, headers, HttpStatus.OK);
//	}

}
