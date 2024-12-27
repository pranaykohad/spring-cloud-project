package com.urbanShows.eventService.entity;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "EVENT_SEAT_TYPE")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventSeatType {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private String seatName;

	private String description;

	private BigDecimal pricePerSeat;

	private int totalSeats;

	private int quotaPerUser;

	private boolean isSoldOut;

}
