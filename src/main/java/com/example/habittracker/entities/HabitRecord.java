package com.example.habittracker.entities;

import java.time.LocalDate;
import java.util.Date;
import java.util.Map;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class HabitRecord {
	public HabitRecord(LocalDate completionDate, Habit habit) {
		this.date = completionDate;
		this.habit = habit;
	}
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private LocalDate date;
	@ManyToOne
	private Habit habit;

}
