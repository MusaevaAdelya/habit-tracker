package com.example.habittracker.entities;

import java.util.Date;
import java.util.Map;

import lombok.Data;

@Data
public class HabitRecord {
	private Map<Date, Boolean> days;

}
