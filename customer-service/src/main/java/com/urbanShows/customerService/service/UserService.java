package com.urbanShows.customerService.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.urbanShows.customerService.dto.UserInfoDto;
import com.urbanShows.customerService.entity.UserInfo;
import com.urbanShows.customerService.exceptionHandler.UserAlreadyExistsException;
import com.urbanShows.customerService.mapper.CustomerMapper;
import com.urbanShows.customerService.repository.UserInfoRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {

	private UserInfoRepository userRepo;
	private ModelMapper modelMapper;
	private PasswordEncoder passwordEncoder;

	public Boolean addUser(UserInfoDto userInfoDto) {
		userInfoDto.setPassword(passwordEncoder.encode(userInfoDto.getPassword()));
		CustomerMapper<UserInfoDto, UserInfo> mapper = new CustomerMapper<>(modelMapper, UserInfoDto.class,
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
		CustomerMapper<UserInfoDto, UserInfo> mapper = new CustomerMapper<>(modelMapper, UserInfoDto.class,
				UserInfo.class);
		return mapper.entityToDto(byName);
	}

	public List<UserInfoDto> getUsersList() {
		CustomerMapper<UserInfoDto, UserInfo> mapper = new CustomerMapper<>(modelMapper, UserInfoDto.class,
				UserInfo.class);
		List<UserInfo> all = userRepo.findAll();
		return mapper.entityToDto(all);
	}

	public void deleteUserByName(String name) {
		userRepo.deleteByName(name);
	}

	public UserInfoDto udpate(UserInfoDto userInfoDto) {
		CustomerMapper<UserInfoDto, UserInfo> mapper = new CustomerMapper<>(modelMapper, UserInfoDto.class,
				UserInfo.class);
		UserInfo save = userRepo.save(mapper.dtoToEntity(userInfoDto));
		return mapper.entityToDto(save);
	}

}
