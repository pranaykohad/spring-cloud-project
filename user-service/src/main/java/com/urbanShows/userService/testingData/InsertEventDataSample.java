package com.urbanShows.userService.testingData;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.urbanShows.userService.entity.UserInfo;
import com.urbanShows.userService.enums.Role;
import com.urbanShows.userService.enums.Status;
import com.urbanShows.userService.repository.UserInfoRepository;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class InsertEventDataSample {
	
	private final PasswordEncoder passwordEncoder;

	private final String[] userNames = { "Aarav", "Vivaan", "Aditya", "Vihaan", "Arjun", "Sai", "Aryan", "Kabir",
			"Krishna", "Atharv", "Ananya", "Aanya", "Saanvi", "Diya", "Riya", "Ishita", "Anika", "Meera", "Aditi",
			"Pooja", "Lakshay", "Raghav", "Dhruv", "Harsh", "Manav", "Rohan", "Yash", "Nikhil", "Siddharth", "Kunal",
			"Sneha", "Priya", "Radhika", "Neha", "Simran", "Tanya", "Kritika", "Shruti", "Aparna", "Swati", "Arnav",
			"Om", "Ishaan", "Rudra", "Dev", "Kartik", "Shaurya", "Tanmay", "Raj", "Abhay", "Trisha", "Nandini",
			"Madhuri", "Juhi", "Kavya", "Ritika", "Anjali", "Sunita", "Mansi", "Kiran", "Aakash", "Keshav", "Viraj",
			"Pranav", "Rahul", "Saket", "Varun", "Vivek", "Gautam", "Sameer", "Chitra", "Poonam", "Malini", "Bhavna",
			"Shreya", "Ayesha", "Jhanvi", "Sonia", "Leela", "Deepa", "Ramesh", "Suresh", "Mahesh", "Ravi", "Vikas",
			"Pankaj", "Amit", "Kishore", "Ajay", "Arvind", "Radha", "Geeta", "Mala", "Savita", "Bina", "Rekha", "Lata",
			"Indira", "Meenakshi", "Sandhya" };

	private final String[] phoneNumbers = { "9876543210", "9123456789", "9988776655", "9123123123", "9878901234",
			"9998887776", "9121121121", "9876709876", "9988998899", "9123987654" };

	private final String[] otps = { "111111", "222222", "333333", "444444", "555555", "666666", "777777", "888888",
			"999999", "000000" };

	private UserInfoRepository userInfoRepository;

	public void insertUserSampleDate() {

		IntStream.range(1, 105).forEach(i -> {
			final UserInfo userInfo = new UserInfo();

			userInfo.setUserName(userNames[i % userNames.length] + " #" + i);
			userInfo.setPassword(passwordEncoder.encode(userNames[i % userNames.length] + " #" + i));
			userInfo.setEmail(userNames[i % userNames.length] + "@gmail.com");
			userInfo.setPhone(phoneNumbers[i % phoneNumbers.length]);
			userInfo.setDisplayName(userNames[i % userNames.length] + " #" + i);
			final Random random = new Random();
			final Role randomRole = Role.values()[random.nextInt(Role.values().length)];
			if(randomRole.equals(Role.SUPER_ADMIN_USER)) {
				return;
			}
			userInfo.setRoles(List.of(randomRole));
			userInfo.setCreatedAt(LocalDateTime.now());
			userInfo.setStatus(Status.INACTIVE);
			userInfo.setOtp(otps[i % otps.length]);
			userInfo.setOtpTimeStamp(LocalDateTime.now());
			
			userInfoRepository.save(userInfo);
		});
		
	}
	
	public void insertUserUserAdminDate() {
		final List<UserInfo> usersByRoles = userInfoRepository.findByRoles(List.of(Role.SUPER_ADMIN_USER));
		if(usersByRoles.isEmpty()) {
			final UserInfo userInfo = new UserInfo();
			userInfo.setUserName("pranay");
			userInfo.setPassword(passwordEncoder.encode("pranay"));
			userInfo.setEmail("pranay@gmail.com");
			userInfo.setPhone("9096987233");
			userInfo.setDisplayName("Pranay Kohad");
			userInfo.setRoles(List.of(Role.SUPER_ADMIN_USER));
			userInfo.setCreatedAt(LocalDateTime.now());
			userInfo.setStatus(Status.ACTIVE);
			userInfo.setOtp("111111");
			userInfo.setPhoneValidated(true);
			userInfo.setEmailValidated(true);
			userInfo.setOtpTimeStamp(LocalDateTime.now());
			userInfoRepository.save(userInfo);
		}
	}

}
