package com.urbanShows.userService.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.urbanShows.userService.dto.AppUserInfoDto;
import com.urbanShows.userService.dto.SystemUserInfoDto;
import com.urbanShows.userService.entity.AppUserInfo;
import com.urbanShows.userService.entity.SystemUserInfo;
import com.urbanShows.userService.entity.Role;
import com.urbanShows.userService.exceptionHandler.UnauthorizedException;
import com.urbanShows.userService.exceptionHandler.UserAlreadyExistsException;
import com.urbanShows.userService.mapper.GenericMapper;
import com.urbanShows.userService.repository.SystemUserInfoRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SystemUserService {

	private SystemUserInfoRepository userRepo;
	private ModelMapper modelMapper;
	private PasswordEncoder passwordEncoder;

	public Boolean addSystemUser(SystemUserInfoDto backendUserInfoDto) {
		if (backendUserInfoDto.getRoles().contains(Role.SYSTEM_USER)) {
			backendUserInfoDto.setPassword(passwordEncoder.encode(backendUserInfoDto.getPassword()));
			GenericMapper<SystemUserInfoDto, SystemUserInfo> mapper = new GenericMapper<>(modelMapper,
					SystemUserInfoDto.class, SystemUserInfo.class);
			SystemUserInfo finalUser = mapper.dtoToEntity(backendUserInfoDto);
			Optional<SystemUserInfo> existingUser = userRepo.findById(finalUser.getUserName());
			if (existingUser.isPresent()) {
				throw new UserAlreadyExistsException("This name already taken, please choose another name");
			}
			userRepo.save(finalUser);
			return true;
		} else {
			throw new UnauthorizedException("You do not have the proper authority to access this resource. Your current Role is " + backendUserInfoDto.getRoles());
		}
	}

	public SystemUserInfoDto getSystemUserByName(String name) {
		SystemUserInfo byName = userRepo.findByUserName(name);
		GenericMapper<SystemUserInfoDto, SystemUserInfo> mapper = new GenericMapper<>(modelMapper,
				SystemUserInfoDto.class, SystemUserInfo.class);
		return mapper.entityToDto(byName);
	}

	public List<SystemUserInfoDto> getSystemUsersList() {
		GenericMapper<SystemUserInfoDto, SystemUserInfo> mapper = new GenericMapper<>(modelMapper,
				SystemUserInfoDto.class, SystemUserInfo.class);
		List<SystemUserInfo> all = userRepo.findAll();
		return mapper.entityToDto(all);
	}

	public void deleteUserByName(String name) {
		userRepo.deleteByUserName(name);
	}

	public SystemUserInfoDto udpate(SystemUserInfoDto userInfoDto) {
		GenericMapper<SystemUserInfoDto, SystemUserInfo> mapper = new GenericMapper<>(modelMapper,
				SystemUserInfoDto.class, SystemUserInfo.class);
		SystemUserInfo save = userRepo.save(mapper.dtoToEntity(userInfoDto));
		return mapper.entityToDto(save);
	}

}
