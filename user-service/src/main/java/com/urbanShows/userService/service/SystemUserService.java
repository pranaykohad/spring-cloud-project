package com.urbanShows.userService.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.urbanShows.userService.dto.SystemUserInfoDto;
import com.urbanShows.userService.entity.Role;
import com.urbanShows.userService.entity.SystemUserInfo;
import com.urbanShows.userService.exceptionHandler.AccessDeniedException;
import com.urbanShows.userService.exceptionHandler.UserAlreadyExistsException;
import com.urbanShows.userService.exceptionHandler.UserNotFoundException;
import com.urbanShows.userService.kafka.KafkaTopicEnums;
import com.urbanShows.userService.kafka.MessageProducer;
import com.urbanShows.userService.kafka.OtpkafkaDto;
import com.urbanShows.userService.mapper.GenericMapper;
import com.urbanShows.userService.repository.SystemUserInfoRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SystemUserService {

	private final SystemUserInfoRepository userRepo;
	private final ModelMapper modelMapper;
	private final PasswordEncoder passwordEncoder;
	private final MessageProducer messageProducer;
	private final OtpService otpService;

	public Boolean addSystemUser(SystemUserInfoDto systemUserDto) {
		if (systemUserDto.getRoles().contains(Role.SYSTEM_USER)) {
			systemUserDto.setPassword(passwordEncoder.encode(systemUserDto.getPassword()));
			final GenericMapper<SystemUserInfoDto, SystemUserInfo> mapper = new GenericMapper<>(modelMapper,
					SystemUserInfoDto.class, SystemUserInfo.class);
			final SystemUserInfo systemUser = mapper.dtoToEntity(systemUserDto);
			final Optional<SystemUserInfo> existingUser = userRepo.findById(systemUser.getUserName());
			if (existingUser.isPresent()) {
				throw new UserAlreadyExistsException("User already exists in the system");
			}
			userRepo.save(systemUser);
			return true;
		} else {
			throw new AccessDeniedException("You are not authorized to do this operation");
		}
	}

	public void uploadProfilePic(SystemUserInfo systemUser, String profilePicUrl) {
		systemUser.setProfilePicUrl(profilePicUrl);
		userRepo.save(systemUser);
	}

	public SystemUserInfo isSystemUserExists(String userName) {
		final SystemUserInfo existingUser = userRepo.findByUserName(userName);
		if (existingUser == null) {
			throw new UserNotFoundException("User doesnot exists in the system");
		}
		return existingUser;
	}

	public List<SystemUserInfoDto> getSystemUsersList() {
		final GenericMapper<SystemUserInfoDto, SystemUserInfo> mapper = new GenericMapper<>(modelMapper,
				SystemUserInfoDto.class, SystemUserInfo.class);
		final List<SystemUserInfo> all = userRepo.findAll();
		return mapper.entityToDto(all);
	}

	@Transactional
	public void deleteSystemUserByUserName(SystemUserInfoDto systemUserDto) {
		final GenericMapper<SystemUserInfoDto, SystemUserInfo> mapper = new GenericMapper<>(modelMapper,
				SystemUserInfoDto.class, SystemUserInfo.class);
		userRepo.delete(mapper.dtoToEntity(systemUserDto));
	}

	public SystemUserInfoDto udpate(SystemUserInfoDto userInfoDto) {
		final GenericMapper<SystemUserInfoDto, SystemUserInfo> mapper = new GenericMapper<>(modelMapper,
				SystemUserInfoDto.class, SystemUserInfo.class);
		final SystemUserInfo save = userRepo.save(mapper.dtoToEntity(userInfoDto));
		return mapper.entityToDto(save);
	}
 
	public void generateOtpForSystemUser(String userName) { 
		final SystemUserInfo systemUser = isSystemUserExists(userName);
		otpService.createOtpForSystemUser(systemUser);
	}
	
	public void sendOtpToPhone(String phone, String otp) {
		final OtpkafkaDto otpkafkaDto = new OtpkafkaDto(phone, otp);
		messageProducer.sendOtpMessage(KafkaTopicEnums.SEND_OTP_TO_USER.name(), otpkafkaDto);
	}

}
