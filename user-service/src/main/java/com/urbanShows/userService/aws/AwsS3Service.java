package com.urbanShows.userService.aws;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.urbanShows.userService.exception.GenericException;
import com.urbanShows.userService.exception.InvalidFileFormatException;
import com.urbanShows.userService.util.Helper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

@Service
@RequiredArgsConstructor
@Slf4j
public class AwsS3Service {

	private final S3Client s3Client;

	@Value("${aws.s3.bucket-name}")
	private String bucketName;

	public String uploadFile(MultipartFile file, String folderName, String fileName) {
		final String key = folderName + "/" + fileName + "." + Helper.getFileExtension(file.getOriginalFilename());
		try {
			Helper.validateBlob(file.getOriginalFilename(), file.getSize());
			final ListObjectsV2Response imageList = s3Client.listObjectsV2(
					ListObjectsV2Request.builder().bucket(bucketName).prefix(folderName + "/" + fileName).build());
			imageList.contents().stream().filter(image -> {
				final String keyName = image.key();
				final int slash = keyName.lastIndexOf('/');
				final int dot = keyName.lastIndexOf('.');
				String existingFileName;
				if (dot > slash) {
					existingFileName = keyName.substring(slash + 1, dot);
				} else {
					existingFileName = keyName.substring(slash + 1);
				}
				return existingFileName.equals(fileName);
			}).forEach(s3Object -> {
				log.info("Deleting existing file with same name: {}", s3Object.key());
				s3Client.deleteObject(DeleteObjectRequest.builder().bucket(bucketName).key(s3Object.key()).build());
			});

			log.info("Uploading file to AWS S3 bucket: {}", key);
			s3Client.putObject(
					PutObjectRequest.builder().bucket(bucketName).key(key).contentType(file.getContentType()).build(),
					RequestBody.fromBytes(file.getBytes()));
			log.info("file: {} is uploaded/replaced in AWS S3 bucket", key);
		} catch(S3Exception e) {
			log.error("S3 Bucket configuration error: {}", e.getMessage());
			throw new GenericException("Not able to upload file in System. Please contact system administrator.");
		} 
		catch (IOException e) {
			log.error("IOException while uploading file to AWS S3: {}", e.getMessage());
			throw new GenericException("Error while uploading file on AWS s3 for system user");
		} catch (InvalidFileFormatException ex) {
			log.error("InvalidFileFormatException while uploading file to AWS S3: {}", ex.getMessage());
			throw new GenericException("File format is not correct: " + file.getOriginalFilename());
		} 
		return key;
	}

	public byte[] downloadFile(String fileName, String folderName) {
		final String key = folderName + "/" + fileName;
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

	public Object downloadFile(String key) {
		try {
			log.info("Downloading file from AWS S3 bucket: {}", key);
			ResponseBytes<GetObjectResponse> objectBytes = s3Client
					.getObjectAsBytes(GetObjectRequest.builder().bucket(bucketName).key(key).build());
			log.info("file: {} is downloaded from AWS S3 bucket", key);
			return Base64.getEncoder().encodeToString(objectBytes.asByteArray());
		} catch (Exception ex) {
			log.error("Exception while downloading file from AWS S3: {}", ex.getMessage());
			throw new GenericException("File not found in s3 bucket: " + ex.getMessage());
		}
	}

	public String deleteFile(String fileName, String folderName) {
		final String key = folderName + "/" + fileName;
		try {
			log.info("Deleting file from AWS S3 bucket: {}", key);
			s3Client.deleteObject(DeleteObjectRequest.builder().bucket(bucketName).key(key).build());
			log.info("file: {} is deleted from AWS S3 bucket", key);
			return "File deleted successfully";
		} catch (Exception ex) {
			log.error("Exception while deleting file from AWS S3: {}", ex.getMessage());
			throw new GenericException("Error while deleting file from s3 bucket: " + ex.getMessage());
		}
	}

}