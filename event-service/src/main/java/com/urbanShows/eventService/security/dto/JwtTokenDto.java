package com.urbanShows.eventService.security.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.urbanShows.eventService.security.enums.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtTokenDto {

	private String token;

	private String username;

	private List<Role> roles;

	private LocalDateTime expiration;

}
