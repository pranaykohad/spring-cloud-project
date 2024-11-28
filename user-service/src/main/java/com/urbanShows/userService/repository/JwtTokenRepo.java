package com.urbanShows.userService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.urbanShows.userService.entity.JwtToken;

import io.micrometer.observation.annotation.Observed;

@Repository
@Observed
public interface JwtTokenRepo extends JpaRepository<JwtToken, String> {

	JwtToken findByToken(String token);

	void deleteByUsername(String username);
}
