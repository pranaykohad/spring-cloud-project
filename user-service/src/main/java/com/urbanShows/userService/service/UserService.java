package com.urbanShows.userService.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.urbanShows.userService.dto.UserInfoDto;
import com.urbanShows.userService.entity.UserInfo;
import com.urbanShows.userService.exceptionHandler.UserAlreadyExistsException;
import com.urbanShows.userService.mapper.GenericMapper;
import com.urbanShows.userService.repository.UserInfoRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {

	private UserInfoRepository userRepo;
	private ModelMapper modelMapper;
	private PasswordEncoder passwordEncoder;

	public Boolean addUser(UserInfoDto userInfoDto) {
		userInfoDto.setPassword(passwordEncoder.encode(userInfoDto.getPassword()));
		GenericMapper<UserInfoDto, UserInfo> mapper = new GenericMapper<>(modelMapper, UserInfoDto.class,
				UserInfo.class);
		UserInfo dtoToEntity = mapper.dtoToEntity(userInfoDto);
		Optional<UserInfo> byId = userRepo.findById(dtoToEntity.getName());
		if (byId.isPresent()) {
			throw new UserAlreadyExistsException("This name already taken, please choose another name");
		}
		userRepo.save(dtoToEntity);
		return true;
	}

	public UserInfoDto getUserByName(String name) {
		UserInfo byName = userRepo.findByName(name);
		GenericMapper<UserInfoDto, UserInfo> mapper = new GenericMapper<>(modelMapper, UserInfoDto.class,
				UserInfo.class);
		return mapper.entityToDto(byName);
	}

	public List<UserInfoDto> getUsersList() {
		GenericMapper<UserInfoDto, UserInfo> mapper = new GenericMapper<>(modelMapper, UserInfoDto.class,
				UserInfo.class);
		List<UserInfo> all = userRepo.findAll();
		all.forEach(System.out::println);
		return mapper.entityToDto(all);
	}

	public void deleteUserByName(String name) {
		userRepo.deleteByName(name);
	}

	public UserInfoDto udpate(UserInfoDto userInfoDto) {
		GenericMapper<UserInfoDto, UserInfo> mapper = new GenericMapper<>(modelMapper, UserInfoDto.class,
				UserInfo.class);
		UserInfo save = userRepo.save(mapper.dtoToEntity(userInfoDto));
		return mapper.entityToDto(save);
	}

}
