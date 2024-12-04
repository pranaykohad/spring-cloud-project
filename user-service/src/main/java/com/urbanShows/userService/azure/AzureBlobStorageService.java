package com.urbanShows.userService.azure;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.urbanShows.userService.dto.AppUserInfoDto;
import com.urbanShows.userService.entity.Role;
import com.urbanShows.userService.entity.SystemUserInfo;
import com.urbanShows.userService.exceptionHandler.AccessDeniedException;
import com.urbanShows.userService.exceptionHandler.FileSizeExceedsException;
import com.urbanShows.userService.exceptionHandler.GenericException;
import com.urbanShows.userService.exceptionHandler.InvalidFileFormatException;
import com.urbanShows.userService.exceptionHandler.UserNotFoundException;
import com.urbanShows.userService.service.AppUserService;
import com.urbanShows.userService.service.SystemUserService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class AzureBlobStorageService {

	private AzureConfig azureConfig;

	private AppUserService appUserService;

	private SystemUserService systemUserService;

	public String uploadSystemUserProfile(MultipartFile file, String userName) {
		final SystemUserInfo existingSystsemUser = systemUserService.isSystemUserExists(userName);
		if (existingSystsemUser == null) {
			throw new UserNotFoundException("User doesnot exists in the system");
		}
		final String originalFileName = file.getOriginalFilename();
		if (!isValidFormat(originalFileName)) {
			throw new InvalidFileFormatException("File format is not correct: " + originalFileName);
		}
		if (!isFileSizeExceeds(file.getSize())) {
			throw new FileSizeExceedsException("File size exceeds: " + file.getSize());
		}
		try {
			final String fileUrl = uploadFile(file);
			log.info("file: {} is uploaded/replaced in azure container", originalFileName);
			systemUserService.uploadProfilePic(existingSystsemUser, fileUrl);
			return fileUrl;
		} catch (Exception e) {
			throw new GenericException("Error while uploading file on Azure Storage");
		}
	}

	public String uploadAppUserProfile(MultipartFile file, String phoneNumber, String otp, String role) {
		final AppUserInfoDto appUserDto = new AppUserInfoDto();
		appUserDto.setPhone(phoneNumber);
		appUserDto.setOtp(otp);
		final List<Role> roleList = new ArrayList<>();
		for(Role item:  Role.values()) {
			if(role.equals(item.name())) {
				roleList.add(item);
			}
		}
		if(roleList.isEmpty()) {
			throw new AccessDeniedException("Your role is empty");
		}
		appUserDto.setRoles(roleList);
		final AppUserInfoDto existingAppUser = appUserService.authenticateAppUserByOtp(appUserDto);
		final String originalFileName = file.getOriginalFilename();
		if (!isValidFormat(originalFileName)) {
			throw new InvalidFileFormatException("File format is not correct: " + originalFileName);
		}
		if (!isFileSizeExceeds(file.getSize())) {
			throw new FileSizeExceedsException("File size exceeds: " + file.getSize());
		}
		try {
			final String fileUrl = uploadFile(file);
			log.info("file: {} is uploaded/replaced in azure container", originalFileName);
			appUserService.uploadProfilePicUrl(existingAppUser, fileUrl);
			return fileUrl;
		} catch (Exception e) {
			throw new GenericException("Error while uploading file on Azure Storage");
		}
	}

	public String uploadFile(MultipartFile file) {
		final BlobContainerClient blobContainerClient = azureConfig.getBlobContainerClient();
		blobContainerClient.createIfNotExists();
		final String imageBlobName = file.getOriginalFilename();
		final BlobClient blobClient = blobContainerClient.getBlobClient(imageBlobName);
		try {
			blobClient.upload(file.getInputStream(), file.getSize(), true);
			return blobClient.getBlobUrl();
		} catch (IOException e) {
			log.error("Error while uploading file: {} to Azure container: {} ", file.getOriginalFilename(),
					blobContainerClient.getBlobContainerName());
		}
		return null;
	}

	private boolean isFileSizeExceeds(long size) {
		return size < AzureConfig.MAX_IMAGE_SIZE;
	}

	private boolean isValidFormat(String fileName) {
		final String fileExtension = getFileExtension(fileName);
		return fileExtension != null && AzureConfig.VALID_IMAGE_FORMAT.contains(fileExtension.toLowerCase());
	}

	private String getFileExtension(String fileName) {
		int lastDotIndex = fileName.lastIndexOf('.');
		return (lastDotIndex != -1 && lastDotIndex < fileName.length() - 1) ? fileName.substring(lastDotIndex + 1)
				: null;
	}

	public byte[] downloadFile(String blobName) {
		return azureConfig.getBlobContainerClient().getBlobClient(blobName).downloadContent().toBytes();
	}

}
