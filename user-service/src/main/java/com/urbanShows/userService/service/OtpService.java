package com.urbanShows.userService.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.urbanShows.userService.entity.AppUser;
import com.urbanShows.userService.entity.SystemUser;
import com.urbanShows.userService.kafka.KafkaTopicEnums;
import com.urbanShows.userService.kafka.MessageProducer;
import com.urbanShows.userService.kafka.OtpkafkaDto;
import com.urbanShows.userService.repository.AppUserRepository;
import com.urbanShows.userService.repository.UserInfoRepository;
import com.urbanShows.userService.util.AuthTokenAndPasswordUtil;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class OtpService {

	private final AppUserRepository appUserInfoRepo;
	private final UserInfoRepository userRepo;
	private final MessageProducer messageProducer;

	public void createOtpForAppUser(AppUser existingAppUser) {
		existingAppUser.setOtp(AuthTokenAndPasswordUtil.generateAuthToken());
		existingAppUser.setOtpTimeStamp(LocalDateTime.now());
		appUserInfoRepo.save(existingAppUser);
		sendOtpToDevice(existingAppUser.getPhone(), existingAppUser.getOtp());
	}

	public void createAndSendOtp(SystemUser userInfo, String device) {
		userInfo.setOtp(AuthTokenAndPasswordUtil.generateAuthToken());
		userInfo.setOtpTimeStamp(LocalDateTime.now());
		userRepo.save(userInfo);
		if (device.equals("EMAIL")) {
			sendOtpToDevice(userInfo.getEmail(), userInfo.getOtp());
		} else {
			sendOtpToDevice(userInfo.getPhone(), userInfo.getOtp());
		}

	}

	private void sendOtpToDevice(String device, String otp) {
		final OtpkafkaDto otpkafkaDto = new OtpkafkaDto(device, otp);
		messageProducer.sendOtpMessage(KafkaTopicEnums.SEND_OTP_TO_USER.name(), otpkafkaDto);
	}

}
