package com.urbanShows.userService.dto;

import org.springframework.http.MediaType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttachmentDto {

	private String filename;
	private byte[] fileContent;
	private MediaType mediaType;

}
