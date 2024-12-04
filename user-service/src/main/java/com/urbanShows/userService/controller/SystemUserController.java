package com.urbanShows.userService.controller;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.urbanShows.userService.dto.ModifierUserDto;
import com.urbanShows.userService.dto.SystemUserInfoDto;
import com.urbanShows.userService.dto.UserUpdateDto;
import com.urbanShows.userService.entity.SystemUserInfo;
import com.urbanShows.userService.mapper.GenericMapper;
import com.urbanShows.userService.service.SystemUserService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("api/user/system")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class SystemUserController {
	
	private final SystemUserService systemUserService;
	private final ModelMapper modelMapper;
	
	@DeleteMapping("remove")
	public ResponseEntity<Boolean> deleteUser(@Valid @RequestBody SystemUserInfoDto systemUser) {
		systemUserService.deleteSystemUserByUserName(systemUser);
		return ResponseEntity.ok(true);
	}
	
	@GetMapping("get-by-username")
	public ResponseEntity<SystemUserInfoDto> getUserByName(@RequestParam String userName) {
		SystemUserInfo existingUser = systemUserService.getSystemUserByUserName(userName);
		GenericMapper<SystemUserInfoDto, SystemUserInfo> mapper = new GenericMapper<>(modelMapper,
				SystemUserInfoDto.class, SystemUserInfo.class);
		return ResponseEntity.ok(mapper.entityToDto(existingUser));
	}

	@PreAuthorize("hasAuthority('ROLE_SYSTEM_USER')")
	@GetMapping("list")
	public ResponseEntity<List<SystemUserInfoDto>> getUsersList() {
		return ResponseEntity.ok(systemUserService.getSystemUsersList());
	}

	@PatchMapping("udpate")
	@PreAuthorize("hasAuthority('ROLE_SYSTEM_USER')")
	public ResponseEntity<Boolean> udpateUser(@Valid @RequestBody UserUpdateDto userUpdateDto) {
		final SystemUserInfoDto systemUserDto = new SystemUserInfoDto();
		systemUserDto.setUserName(userUpdateDto.getModifierUserDto().getUserName()); 
		systemUserDto.setPhone(userUpdateDto.getModifierUserDto().getPhone()); 
		systemUserDto.setOtp(userUpdateDto.getModifierUserDto().getOtp()); 
		systemUserService.authenticateSystemUserByOtp(systemUserDto.getUserName(), systemUserDto.getOtp());
		return ResponseEntity.ok(systemUserService.udpateUserDetails(userUpdateDto));
	}

}
