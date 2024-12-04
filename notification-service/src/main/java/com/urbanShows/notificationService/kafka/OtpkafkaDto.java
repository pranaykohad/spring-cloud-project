package com.urbanShows.notificationService.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OtpkafkaDto {

	private String phone;

	private String otp;

}

