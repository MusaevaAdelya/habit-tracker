package com.example.habittracker.dto.request;

import java.util.Date;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class EnableDto {
	

	private String habitName;
	private int goal;
	private int periodicity;
	private Date startDate;
	private Date endDate;
}
