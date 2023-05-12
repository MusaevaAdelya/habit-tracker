package com.example.habittracker.services;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.habittracker.entities.Habit;
import com.example.habittracker.entities.HabitRecord;
import com.example.habittracker.repositories.HabitRecordRepository;
import com.example.habittracker.repositories.HabitRepository;

@Service
public class HabitService {

	@Autowired
	private HabitRepository habitRepository;
	@Autowired
	private HabitRecordRepository habitRecordRepository;
	public Boolean existsByName(String name) {
		return this.habitRepository.existsByName(name);
	}

	public Habit getByName(String name) {
		return this.getByName(name);
	}

	public void save(Habit habit) {
		this.habitRepository.save(habit);
	}

	public void delete(Habit byName) {
		this.habitRepository.delete(byName);

	}

	public String markHabitCompleted(Long habitId, LocalDate completionDate) {
		 Habit habit = habitRepository.findById(habitId).orElseThrow(() -> new RuntimeException("Habit not found"));
		 HabitRecord habitRecord = habitRecordRepository.getByDate(completionDate);
			
			  if (completionDate.isBefore(habit.getStartDate()) ||
			  completionDate.isAfter(habit.getEndDate())) { 
				  return "Completion date is outside habit timeframe";
			  }
			  
			  
			 
			  if ( habitRecord != null) { 
				 
			  return "Habit already completed for date " + completionDate;
			  }
			  
			  
			  int completedDays = habit.getCompletedDays()+1;
			
			  
			  if (completedDays >= habit.getGoalDays()) {
				  return "Goal days exceeded"; }
			  HabitRecord hRecord = new HabitRecord(completionDate,habit);
			  habitRecordRepository.save(hRecord);
			  
			  habit.setCompletedDays(completedDays);
			  habitRepository.save(habit);
			 
	        
	        
		return "Keep going!";
	}
	public void deleteAllFromHabitRecord(int habitId) {
		this.habitRecordRepository.deleteAllByHabitId(habitId);
	}
	public void deleteHabit(String habitName) {
		Habit habit = this.habitRepository.getByName(habitName);
		int habitId = habit.getId();
		deleteAllFromHabitRecord(habitId);
		this.habitRepository.delete(habit);
	
	}
}
