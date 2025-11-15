package com.urbanShows.userService.schedulers;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Component;

import com.urbanShows.userService.entity.AppUser;
import com.urbanShows.userService.entity.SystemUser;
import com.urbanShows.userService.repository.AppUserRepository;
import com.urbanShows.userService.repository.UserInfoRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@AllArgsConstructor
@Slf4j
public class OtpCleanupScheduler {

	private final AppUserRepository appUserInfoRepo;
	private final UserInfoRepository systemUserInfoRepository;

//	@Scheduled(fixedRate = 1000) // check in every 10 sec
	public void appUserOtpCleanup() {
		List<AppUser> appUserListWithOtp = appUserInfoRepo.findByOtpDateTime(LocalDateTime.now().minusSeconds(60));
		appUserListWithOtp.forEach(appUser -> {
			appUser.setOtp("");
			appUserInfoRepo.save(appUser);
			log.debug("Otps are cleared for app users");
		});
	}

//	@Scheduled(fixedRate = 1000) // check in every 10 sec
	public void systemUserOtpCleanup() {
		List<SystemUser> appUserListWithOtp = systemUserInfoRepository
				.findByOtpDateTime(LocalDateTime.now().minusSeconds(60));
		appUserListWithOtp.forEach(appUser -> {
			appUser.setOtp("");
			systemUserInfoRepository.save(appUser);
			log.debug("Otps are cleared for system users");
		});
	}

}
