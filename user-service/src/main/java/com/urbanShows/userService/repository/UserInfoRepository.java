package com.urbanShows.userService.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.urbanShows.userService.entity.UserInfo;
import com.urbanShows.userService.enums.Role;

import io.micrometer.observation.annotation.Observed;

@Repository
@Observed
public interface UserInfoRepository extends JpaRepository<UserInfo, String> {

	UserInfo findByUserName(String userName);

	UserInfo findByUserNameAndPassword(String userName, String password);

	void deleteByUserName(String userName);

	UserInfo findByUserNameAndOtp(String userName, String otp);

	@Query("SELECT a FROM UserInfo a WHERE a.otpTimeStamp < :otpTimeStamp")
	List<UserInfo> findByOtpDateTime(@Param("otpTimeStamp") LocalDateTime otpTimeStamp);

	List<UserInfo> findByRoles(List<Role> list);

}
