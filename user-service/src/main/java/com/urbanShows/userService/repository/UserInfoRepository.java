package com.urbanShows.userService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.urbanShows.userService.entity.UserInfo;

import io.micrometer.observation.annotation.Observed;


@Repository
@Observed
public interface UserInfoRepository extends JpaRepository<UserInfo, String> {

	UserInfo findByName(String username);

	void deleteByName(String name);

}
