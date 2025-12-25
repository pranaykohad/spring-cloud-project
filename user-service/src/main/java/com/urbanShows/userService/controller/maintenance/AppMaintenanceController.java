package com.urbanShows.userService.controller.maintenance;

import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.urbanShows.userService.entity.SystemUser;
import com.urbanShows.userService.enums.Role;
import com.urbanShows.userService.exception.UnauthorizedException;
import com.urbanShows.userService.service.SystemUserService;
import com.urbanShows.userService.testingData.TestDataUtil;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("api/maintainance")
@AllArgsConstructor
public class AppMaintenanceController {

	private final TestDataUtil testDataUtil;
	private final SystemUserService systemUserService;

	@GetMapping("add-admin-details")
	public ResponseEntity<String> addAdminDetails() {
		final String response = testDataUtil.insertSuperAdminData();
		return ResponseEntity.ok(response);
	}

	@GetMapping("add-system-user-data")
	@PreAuthorize("hasAnyAuthority('ROLE_SUPER_ADMIN_USER')")
	public ResponseEntity<String> addSystemUserData(@RequestParam String superAdminOtp, Principal principal) {
		final SystemUser currentUser = systemUserService.validateActiveSystemUserByOtp(principal.getName(),
				superAdminOtp);
		if (currentUser.getRoles().contains(Role.SUPER_ADMIN_USER)) {
			final String response = testDataUtil.insertSystemUserTestData();
			return ResponseEntity.ok(response);
		} else {
			throw new UnauthorizedException("You cannot perform this operation");
		}
	}

}
