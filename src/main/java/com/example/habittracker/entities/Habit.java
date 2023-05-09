package com.example.habittracker.entities;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Habit {

	public Habit(String habitName) {
		this.name = habitName;
	}
	public Habit(String habitName, int periodicity, Date startDate, Date endDate, boolean enable) {
		this.name = habitName;
		this.periodicity = periodicity;
		this.startDate = startDate;
		this.endDate = endDate;
		this.enable = enable;
	}
	
	public Habit(String habitName, Userr user) {
		this.name = habitName;
		this.user = user;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String name;
	private String description;
	private int goal;
	private int periodicity;
	
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date startDate;
	
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date endDate;
	private boolean enable;
	
	@ManyToOne
	private Userr user;
	

	
}
