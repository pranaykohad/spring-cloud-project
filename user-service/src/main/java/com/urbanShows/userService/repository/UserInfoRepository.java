package com.urbanShows.userService.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.urbanShows.userService.entity.UserInfo;
import com.urbanShows.userService.enums.Role;
import com.urbanShows.userService.enums.Status;

import io.micrometer.observation.annotation.Observed;

@Repository
@Observed
public interface UserInfoRepository extends JpaRepository<UserInfo, String>, JpaSpecificationExecutor<UserInfo> {

	UserInfo findByUserName(String userName);

	UserInfo findByUserNameAndPassword(String userName, String password);

	void deleteByUserName(String userName);

	UserInfo findByUserNameAndOtpAndStatus(String userName, String otp, Status status);

	@Query("SELECT a FROM UserInfo a WHERE a.otpTimeStamp < :otpTimeStamp")
	List<UserInfo> findByOtpDateTime(@Param("otpTimeStamp") LocalDateTime otpTimeStamp);

	List<UserInfo> findByRoles(List<Role> list);

	List<UserInfo> findByRolesAndStatus(List<Role> list, Status status);

	UserInfo findByUserNameAndRolesAndStatus(String username, List<Role> list, Status status);

}
