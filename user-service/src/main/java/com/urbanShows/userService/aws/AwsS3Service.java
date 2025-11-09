package com.urbanShows.userService.aws;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.urbanShows.userService.exception.GenericException;

import lombok.AllArgsConstructor;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
public class AwsS3Service {

	@Autowired
	private S3Client s3Client;

	@Value("${aws.s3.bucket-name:}")
	private String bucketName;
	
	@Value("${aws.s3.folder-name:}")
	private String folderName;

	public String uploadFile(MultipartFile file) {	
		final String key =  folderName + "/" + file.getOriginalFilename();
		try {
			s3Client.putObject(PutObjectRequest.builder().bucket(bucketName).key(key).build(),
					RequestBody.fromBytes(file.getBytes()));
		} catch (IOException e) {
			throw new GenericException("Error while uploading file on AWS s3 for system user");
		}
		 return "https://" + bucketName + ".s3.amazonaws.com/" + key;
	}

	public byte[] downloadFile(String blobName) {
		try {
			ResponseBytes<GetObjectResponse> objectBytes = s3Client
					.getObjectAsBytes(GetObjectRequest.builder().bucket(bucketName).key(blobName).build());

			return objectBytes.asByteArray();
		} catch (Exception ex) {
			throw new GenericException("File not found in s3 bucket: " + ex.getMessage());
		}
	}

}
