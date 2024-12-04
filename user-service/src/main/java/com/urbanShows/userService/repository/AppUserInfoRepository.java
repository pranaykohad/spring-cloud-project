package com.urbanShows.userService.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.urbanShows.userService.entity.AppUserInfo;

import io.micrometer.observation.annotation.Observed;

@Repository
@Observed
public interface AppUserInfoRepository extends JpaRepository<AppUserInfo, String> {

	AppUserInfo findByPhone(String phone);

	void deleteByPhone(String phone);

	AppUserInfo findByPhoneAndOtp(String phone, String otp);

	@Query("SELECT a FROM AppUserInfo a WHERE a.otpTimeStamp < :otpTimeStamp")
	List<AppUserInfo> findByOtpDateTime(@Param("otpTimeStamp") LocalDateTime otpTimeStamp);

}
