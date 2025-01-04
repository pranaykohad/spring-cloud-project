package com.urbanShows.userService.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ColumnConfigDto {

	private List<String> columns;

}
