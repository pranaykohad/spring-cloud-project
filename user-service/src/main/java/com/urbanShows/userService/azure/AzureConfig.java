package com.urbanShows.userService.azure;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//import com.azure.storage.blob.BlobContainerClient;
//import com.azure.storage.blob.BlobServiceClient;
//import com.azure.storage.blob.BlobServiceClientBuilder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//@Configuration
//@Data
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
//public class AzureConfig {
//
//	@Value("${azure.storage.connection-string}")
//	private String connectionString;
//
//	@Value("${azure.storage.conatiner.name}")
//	private String containerName;
//
//	public static final List<String> VALID_IMAGE_FORMAT = new ArrayList<>(
//			List.of("jpg", "jpeg", "png", "heic", "webp"));
//
//	public static final long MAX_IMAGE_SIZE = 5242880; // 5 MB = 5242880
//
//	@Bean
//	BlobServiceClient getBlobServiceClient() {
//		return new BlobServiceClientBuilder().connectionString(connectionString).buildClient();
//	}
//
//	@Bean
//	BlobContainerClient getBlobContainerClient() {
//		return getBlobServiceClient().getBlobContainerClient(containerName);
//	}
//
//}
