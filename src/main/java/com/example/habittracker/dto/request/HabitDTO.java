package com.example.habittracker.dto.request;

import java.time.LocalDate;
import java.util.Date;


import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class HabitDTO {

	private String name;
	private String description;
	private int goalDays;
	private int perDay;

}
