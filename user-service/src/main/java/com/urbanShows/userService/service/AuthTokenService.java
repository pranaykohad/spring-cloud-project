//package com.urbanShows.userService.service;
//
//import java.time.LocalDateTime;
//
//import org.modelmapper.ModelMapper;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import com.urbanShows.userService.dto.AuthTokenDto;
//import com.urbanShows.userService.entity.AuthToken;
//import com.urbanShows.userService.exceptionHandler.AccessDeniedException;
//import com.urbanShows.userService.kafka.KafkaTopicEnums;
//import com.urbanShows.userService.kafka.MessageProducer;
//import com.urbanShows.userService.mapper.GenericMapper;
//import com.urbanShows.userService.repository.AuthTokenRepository;
//import com.urbanShows.userService.util.AuthTokenAndPasswordUtil;
//
//import lombok.AllArgsConstructor;
//
//@Service
//@AllArgsConstructor
//public class AuthTokenService {
//
//	private final AuthTokenRepository authTokenRepository;
//	private final MessageProducer messageProducer;
//	private final ModelMapper modelMapper;
//
//	public void removeAuthToken(AuthTokenDto authTokenDto) {
//		GenericMapper<AuthTokenDto, AuthToken> mapper = new GenericMapper<>(modelMapper, AuthTokenDto.class,
//				AuthToken.class);
//		final AuthToken authToken = mapper.dtoToEntity(authTokenDto);
//		authTokenRepository.deleteById(authToken.getPhoneNumber());
//	}
//
//	public AuthTokenDto authenticateAuthToken(AuthTokenDto authTokenDto) {
//		final AuthToken authToken = authTokenRepository.findByIdAndOtp(authTokenDto.getPhoneNumber(),
//				authTokenDto.getAuthToken());
//		if (authToken == null) {
//			throw new AccessDeniedException();
//		}
//		final GenericMapper<AuthTokenDto, AuthToken> mapper = new GenericMapper<>(modelMapper, AuthTokenDto.class,
//				AuthToken.class);
//		return mapper.entityToDto(authToken);
//	}
//	
//	public boolean addPhoneNumber(String phoneNumber) {
//		final AuthToken authToken = new AuthToken();
//		authToken.setPhoneNumber(phoneNumber);
//		authTokenRepository.save(authToken);
//		return true;
//	}
//
//	public boolean saveAndSendAuthToken(final String id) {
//		final AuthToken authToken = new AuthToken();
//		authToken.setPhoneNumber(id);
//		authToken.setAuthToken(AuthTokenAndPasswordUtil.generateAuthToken());
//		authToken.setTokenTimeStamp(LocalDateTime.now());
//		authTokenRepository.save(authToken);
//		messageProducer.sendMessage(KafkaTopicEnums.SEND_OTP_TO_USER.name(), authToken.toString());
//		return true;
//	}
//
//	public AuthToken findByIdAndOtp(String id, String otp) {
//		return authTokenRepository.findByIdAndOtp(id, otp);
//	}
//
//	@Transactional
//	public void deleteById(String phoneNumber) {
//		authTokenRepository.deleteById(phoneNumber);
//	}
//
//}
