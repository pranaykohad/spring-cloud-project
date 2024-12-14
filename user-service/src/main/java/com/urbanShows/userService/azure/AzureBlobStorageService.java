package com.urbanShows.userService.azure;

import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.models.BlobStorageException;
import com.urbanShows.userService.entity.AppUserInfo;
import com.urbanShows.userService.entity.UserInfo;
import com.urbanShows.userService.exceptionHandler.BlobNotFoundException;
import com.urbanShows.userService.exceptionHandler.GenericException;
import com.urbanShows.userService.service.AppUserService;
import com.urbanShows.userService.service.UserService;
import com.urbanShows.userService.util.Helper;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class AzureBlobStorageService {

	private AzureConfig azureConfig;

	private AppUserService appUserService;

	private UserService systemUserService;

	public boolean uploadSystemUserProfile(MultipartFile file, UserInfo systemUser) {
		final String originalFileName = file.getOriginalFilename();
		Helper.validateBlob(originalFileName, file.getSize());
		try {
			final String fileUrl = uploadFile(file);
			log.info("file: {} is uploaded/replaced in azure container", originalFileName);
			systemUserService.uploadSystemUserProfilePicUrl(systemUser, fileUrl);
			return true;
		} catch (Exception e) {
			throw new GenericException("Error while uploading file on Azure Storage for system user");
		}
	}

	public boolean uploadAppUserProfile(MultipartFile file, AppUserInfo appUser) {
		final String originalFileName = file.getOriginalFilename();
		Helper.validateBlob(originalFileName, file.getSize());
		try {
			final String fileUrl = uploadFile(file);
			log.info("file: {} is uploaded/replaced in azure container", originalFileName);
			appUserService.uploadAppUserProfilePicUrl(appUser, fileUrl);
			return true;
		} catch (Exception e) {
			throw new GenericException("Error while uploading file on Azure Storage for app user");
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

	public byte[] downloadFile(String blobName) {
		try {
			return azureConfig.getBlobContainerClient().getBlobClient(blobName).downloadContent().toBytes();
		} catch (BlobStorageException ex) {
			throw new BlobNotFoundException(ex.getLocalizedMessage());
		}
	}

}
