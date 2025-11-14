package com.urbanShows.eventService.azure;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

//import com.azure.core.http.rest.PagedIterable;
//import com.azure.storage.blob.BlobClient;
//import com.azure.storage.blob.BlobContainerClient;
//import com.azure.storage.blob.models.BlobItem;
//import com.azure.storage.blob.models.BlobStorageException;
//import com.azure.storage.blob.models.ListBlobsOptions;
//import com.urbanShows.eventService.security.exception.BlobNotFoundException;
//import com.urbanShows.eventService.security.exception.GenericException;
import com.urbanShows.eventService.security.util.Helper;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

//@Service
//@AllArgsConstructor
//@Slf4j
//public class AzureBlobStorageService {
//
//	private BlobContainerClient blobContainerClient;
//
//	private AzureConfig azureConfig;
//
//	@PostConstruct
//	public void init() {
//		blobContainerClient = azureConfig.getBlobContainerClient();
//		blobContainerClient.createIfNotExists();
//	}
//
//	public String uploadFile(MultipartFile file) {
//		final String originalFileName = file.getOriginalFilename();
//		Helper.validateBlob(originalFileName, file.getSize());
//		try {
//			final String fileUrl = uploadFileInContainer(file);
//			log.info("file: {} is uploaded/replaced in azure container", originalFileName);
//			return fileUrl;
//		} catch (Exception e) {
//			throw new GenericException("Error while uploading file on Azure Storage for system user");
//		}
//	}
//
//	public byte[] downloadFile(String blobName) {
//		try {
//			return blobContainerClient.getBlobClient(blobName).downloadContent().toBytes();
//		} catch (BlobStorageException ex) {
//			throw new BlobNotFoundException(ex.getLocalizedMessage());
//		}
//	}
//
//	public void deleteFileFromContainer(List<String> mediaUrlList) {
//		mediaUrlList.forEach(mediaUrl -> {
//			URI uri;
//			try {
//				uri = new URI(mediaUrl);
//				Path path = Paths.get(uri.getPath());
//				String blobName = path.getFileName().toString();
//				blobContainerClient.getBlobClient(blobName).delete();
//			} catch (URISyntaxException e) {
//				log.error("Error deleting blob: {} from container: {}", mediaUrl, blobContainerClient.getBlobContainerName());
//			}
//
//		});
//	}
//
//	private String uploadFileInContainer(MultipartFile file) {
//		final String mediaFileName = file.getOriginalFilename();
//		final BlobClient blobClient = blobContainerClient.getBlobClient(mediaFileName);
//		try {
//			// Extract blob list from azure container with same name
//			final ListBlobsOptions listOption = new ListBlobsOptions();
//			listOption.setPrefix(Helper.getFileNameWithoutTimeAndExtension(mediaFileName));
//			final PagedIterable<BlobItem> blobList = blobContainerClient.listBlobs(listOption, null, null);
//
//			for (BlobItem blobItem : blobList) {
//				final String blobName = blobItem.getName();
//
//				// Check in new media file name and existing blob name are same and delete blob
//				// from container
//				if (Helper.getFileNameWithoutTimeAndExtension(blobName)
//						.equals(Helper.getFileNameWithoutTimeAndExtension(mediaFileName))) {
//					blobContainerClient.getBlobClient(blobName).delete();
//					log.info("Blob: {} is deleted from conatiner: {}", blobName,
//							blobContainerClient.getBlobContainerName());
//				}
//
//			}
//
//			// Upload new blob in container and return the url
//			blobClient.upload(file.getInputStream(), file.getSize(), true);
//			return blobClient.getBlobUrl();
//		} catch (IOException e) {
//			log.error("Error while uploading file: {} to Azure container: {} ", file.getOriginalFilename(),
//					blobContainerClient.getBlobContainerName());
//		}
//		return null;
//	}
//
//}
