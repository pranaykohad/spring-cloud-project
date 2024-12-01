package com.urbanShows.userService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.urbanShows.userService.entity.SystemUserInfo;

import io.micrometer.observation.annotation.Observed;


@Repository
@Observed
public interface SystemUserInfoRepository extends JpaRepository<SystemUserInfo, String> {

	SystemUserInfo findByUserName(String userName);

	void deleteByUserName(String userName);

}
