package com.urbanShows.userService.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.urbanShows.userService.entity.AppUserInfo;
import com.urbanShows.userService.entity.UserInfo;
import com.urbanShows.userService.kafka.KafkaTopicEnums;
import com.urbanShows.userService.kafka.MessageProducer;
import com.urbanShows.userService.kafka.OtpkafkaDto;
import com.urbanShows.userService.repository.AppUserInfoRepository;
import com.urbanShows.userService.repository.UserInfoRepository;
import com.urbanShows.userService.util.AuthTokenAndPasswordUtil;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class OtpService {

	private final AppUserInfoRepository appUserInfoRepo;
	private final UserInfoRepository userRepo;
	private final MessageProducer messageProducer;

	public void createOtpForAppUser(AppUserInfo existingAppUser) {
		existingAppUser.setOtp(AuthTokenAndPasswordUtil.generateAuthToken());
		existingAppUser.setOtpTimeStamp(LocalDateTime.now());
		appUserInfoRepo.save(existingAppUser);
		sendOtpToPhone(existingAppUser.getPhone(), existingAppUser.getOtp());
	}

	public void createAndSendOtp(UserInfo userInfo, String device) {
		userInfo.setOtp(AuthTokenAndPasswordUtil.generateAuthToken());
		userInfo.setOtpTimeStamp(LocalDateTime.now());
		userRepo.save(userInfo);
		if (device.equals("EMAIL")) {
			sendOtpToPhone(userInfo.getEmail(), userInfo.getOtp());
		} else {
			sendOtpToPhone(userInfo.getPhone(), userInfo.getOtp());
		}

	}

	private void sendOtpToPhone(String phone, String otp) {
		final OtpkafkaDto otpkafkaDto = new OtpkafkaDto(phone, otp);
		messageProducer.sendOtpMessage(KafkaTopicEnums.SEND_OTP_TO_USER.name(), otpkafkaDto);
	}

}
