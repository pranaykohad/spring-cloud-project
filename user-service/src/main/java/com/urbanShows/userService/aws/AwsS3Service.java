package com.urbanShows.userService.aws;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.urbanShows.userService.exception.GenericException;
import com.urbanShows.userService.util.Helper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
@RequiredArgsConstructor
@Slf4j
public class AwsS3Service {

	private final S3Client s3Client;

	@Value("${aws.s3.bucket-name}")
	private String bucketName;
	
	@Value("${aws.s3.folder-name}")
	private String folderName;

	public String uploadFile(MultipartFile file) {	
		Helper.validateBlob(file.getOriginalFilename(), file.getSize());
		final String key =  folderName + "/" + file.getOriginalFilename();
		String fileUrl = null;
		try {
			log.info("Uploading file to AWS S3 bucket: {}", key);
			s3Client.putObject(PutObjectRequest.builder().bucket(bucketName).key(key).build(),
					RequestBody.fromBytes(file.getBytes()));
			log.info("file: {} is uploaded/replaced in AWS S3 bucket", key);
			fileUrl = "https://" + bucketName + ".s3.amazonaws.com/" + key;
		} catch (IOException e) {
			log.error("IOException while uploading file to AWS S3: {}", e.getMessage());
			throw new GenericException("Error while uploading file on AWS s3 for system user");
		}
		 return fileUrl;
	}

	public byte[] downloadFile(String fileName) {
		final String key =  folderName + "/" + fileName;
		try {
			log.info("Downloading file from AWS S3 bucket: {}", key);
			ResponseBytes<GetObjectResponse> objectBytes = s3Client
					.getObjectAsBytes(GetObjectRequest.builder().bucket(bucketName).key(key).build());
			log.info("file: {} is downloaded from AWS S3 bucket", key);
			return objectBytes.asByteArray();
		} catch (Exception ex) {
			log.error("Exception while downloading file from AWS S3: {}", ex.getMessage());
			throw new GenericException("File not found in s3 bucket: " + ex.getMessage());
		}
	}
	
	public String deleteFile(String fileName) {
		final String key =  folderName + "/" + fileName;
		try {
			log.info("Deleting file from AWS S3 bucket: {}", key);
			s3Client.deleteObject(
                    DeleteObjectRequest.builder()
                            .bucket(bucketName)
                            .key(key)
                            .build()
            );
			log.info("file: {} is deleted from AWS S3 bucket", key);
			return "File deleted successfully";
		} catch (Exception ex) {
			log.error("Exception while deleting file from AWS S3: {}", ex.getMessage());
			throw new GenericException("Error while deleting file from s3 bucket: " + ex.getMessage());
		}
	}	

}