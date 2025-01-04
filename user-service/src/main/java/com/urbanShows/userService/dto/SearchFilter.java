package com.urbanShows.userService.dto;

import com.urbanShows.userService.enums.SearchOperator;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SearchFilter {

	@NotNull(message = "key cannot be null")
	private String key;
	
	@NotNull(message = "value cannot be null")
	private Object value;
	
	private Object valueTo;
	
	@NotNull(message = "operator cannot be null")
	private SearchOperator operator;

}
