package com.urbanShows.userService.entity;


import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtToken {

	@Id
	private String token;
	
	private String username;
	
	private List<Role> roles;
	
	private LocalDateTime expiration;

}
