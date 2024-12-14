package com.urbanShows.userService.schedulers;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Component;

import com.urbanShows.userService.entity.AppUserInfo;
import com.urbanShows.userService.entity.UserInfo;
import com.urbanShows.userService.repository.AppUserInfoRepository;
import com.urbanShows.userService.repository.UserInfoRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@AllArgsConstructor
@Slf4j
public class OtpCleanupScheduler {

	private final AppUserInfoRepository appUserInfoRepo;
	private final UserInfoRepository systemUserInfoRepository;

//	@Scheduled(fixedRate = 1000) // check in every 10 sec
	public void appUserOtpCleanup() {
		List<AppUserInfo> appUserListWithOtp = appUserInfoRepo.findByOtpDateTime(LocalDateTime.now().minusSeconds(60));
		appUserListWithOtp.forEach(appUser -> {
			appUser.setOtp("");
			appUserInfoRepo.save(appUser);
			log.debug("Otp is cleared for phone: {}", appUser.getPhone());
		});
	}

//	@Scheduled(fixedRate = 1000) // check in every 10 sec
	public void systemUserOtpCleanup() {
		List<UserInfo> appUserListWithOtp = systemUserInfoRepository
				.findByOtpDateTime(LocalDateTime.now().minusSeconds(60));
		appUserListWithOtp.forEach(appUser -> {
			appUser.setOtp("");
			systemUserInfoRepository.save(appUser);
			log.debug("Otp is cleared for phone: {}", appUser.getPhone());
		});
	}

}
