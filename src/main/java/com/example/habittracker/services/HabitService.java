package com.example.habittracker.services;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.habittracker.entities.Habit;
import com.example.habittracker.repositories.HabitRepository;

@Service
public class HabitService {
	
	@Autowired
	private HabitRepository habitRepository;

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
        
        if (completionDate.isBefore(habit.getStartDate()) || completionDate.isAfter(habit.getEndDate())) {
            throw new RuntimeException("Completion date is outside habit timeframe");
        }
        Boolean completionStatus = null;
        if(habit.getCompletionStatusByDate().get(completionDate) != null) {
        	completionStatus = true;
        }
        if (completionStatus != null && completionStatus) {
            throw new RuntimeException("Habit already completed for date " + completionDate);
        }
        
        int completedDays = habit.getCompletionStatusByDate().values().stream().mapToInt(c -> c ? 1 : 0).sum();
        if (completedDays >= habit.getGoalDays()) {
            throw new RuntimeException("Goal days exceeded");
        }
        
        habit.getCompletionStatusByDate().put(completionDate, true);
        habitRepository.save(habit);
        
        
        return "Keep going!";
}
}
