package com.theHub.model.dto.userDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileResponseDto {

	private Long id;
	private String nick;
	private String status;
	private String biography;
	private boolean followed;
	
}