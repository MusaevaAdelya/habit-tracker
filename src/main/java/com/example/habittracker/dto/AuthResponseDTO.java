package com.example.habittracker.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class AuthResponseDTO {

	private String accessToken;
	private String tokenType = "Bearer ";
	
	public AuthResponseDTO(String accessToken) {
		this.accessToken = accessToken;
	}
	
	
}
