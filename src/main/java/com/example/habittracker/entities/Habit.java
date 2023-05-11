package com.example.habittracker.entities;

import java.time.LocalDate;
import java.util.Date;
import java.util.Map;

import com.example.habittracker.enums.GoalPeriodType;
import com.example.habittracker.enums.HabitType;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
public class Habit {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String name;
	private String description;
	private int goalDays;
	private int perDay;
	private GoalPeriodType goalPeriodType;
	private HabitType habitType;
	
	
	@JsonFormat(pattern="yyyy-MM-dd")
	private LocalDate startDate;
	
	@JsonFormat(pattern="yyyy-MM-dd")
	private LocalDate endDate;
	
	private boolean enable;
	private int completedDays;
	@Column
	@ElementCollection(targetClass=Date.class)
	private Map<LocalDate, Boolean> completionStatusByDate;
	
	@ManyToOne
	private User user;

	public Habit(String name, String description, int goalDays, int perDay, LocalDate startDate, LocalDate endDate) {
		super();
		this.name = name;
		this.description = description;
		this.goalDays = goalDays;
		this.perDay = perDay;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public Habit(String description, int goalDays, int perDay, LocalDate startDate, LocalDate endDate) {
		super();
		this.description = description;
		this.goalDays = goalDays;
		this.perDay = perDay;
		this.startDate = startDate;
		this.endDate = endDate;
	}
	
	
	
}
