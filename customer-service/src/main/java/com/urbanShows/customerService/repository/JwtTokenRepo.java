package com.urbanShows.customerService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.urbanShows.customerService.entity.JwtToken;

@Repository
public interface JwtTokenRepo extends JpaRepository<JwtToken, String> {

	JwtToken findByToken(String token);

	void deleteByUsername(String username);
}
