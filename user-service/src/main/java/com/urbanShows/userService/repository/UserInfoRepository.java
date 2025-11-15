package com.urbanShows.userService.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.urbanShows.userService.entity.SystemUser;
import com.urbanShows.userService.enums.Role;
import com.urbanShows.userService.enums.Status;

import io.micrometer.observation.annotation.Observed;

@Repository
@Observed
public interface UserInfoRepository extends JpaRepository<SystemUser, String>, JpaSpecificationExecutor<SystemUser> {
	
	SystemUser findByUserName(String userName);

	SystemUser findByUserNameAndStatus(String userName, Status status);

	SystemUser findByUserNameAndPassword(String userName, String password);

	void deleteByUserName(String userName);

	SystemUser findByUserNameAndOtpAndStatus(String userName, String otp, Status status);

	@Query("SELECT a FROM UserInfo a WHERE a.otpTimeStamp < :otpTimeStamp")
	List<SystemUser> findByOtpDateTime(@Param("otpTimeStamp") LocalDateTime otpTimeStamp);

	List<SystemUser> findByRoles(List<Role> list);

	List<SystemUser> findByRolesAndStatus(List<Role> list, Status status);

	SystemUser findByUserNameAndRolesAndStatus(String username, List<Role> list, Status status);

}
