package com.example.habittracker.controllers;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import com.example.habittracker.dto.request.HabitDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.habittracker.entities.Habit;
import com.example.habittracker.entities.HabitRecord;
import com.example.habittracker.entities.User;
import com.example.habittracker.enums.GoalPeriodType;
import com.example.habittracker.repositories.HabitRepository;
import com.example.habittracker.repositories.UserRepository;
import com.example.habittracker.services.HabitService;

@RestController
@RequestMapping("/main")
public class HabitController {

	@Autowired
	private HabitRepository habitRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private HabitService habitService;
	@PostMapping("/addHabit")
	public ResponseEntity<?> addHabit(@RequestBody HabitDTO habit){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = this.userRepository.getByEmail(auth.getName());
		LocalDate currentDate = LocalDate.now();
		
		if(this.habitRepository.existsByName(habit.getName())) {
	        
			
			Habit newHabit = this.habitRepository.getByName(habit.getName());
			newHabit.setPerDay(habit.getPerDay());
			
			if(currentDate.isBefore(habit.getStartDate())) {
				newHabit.setStartDate(habit.getStartDate());
			}
			else {
				return new ResponseEntity<String>("Please select a proper date!",HttpStatus.BAD_REQUEST);
 
			}
			newHabit.setEndDate(habit.getEndDate());
			newHabit.setGoalDays(habit.getGoalDays());
			newHabit.setDescription(habit.getDescription());
			newHabit.setEnable(true);
			newHabit.setUser(user);
			this.habitRepository.save(newHabit);
			return new ResponseEntity<String>("Congrats! You started a new habit! Good luck!", HttpStatus.OK);
		}
		
			Habit newHabit = new Habit();
			
			
			if(currentDate.isBefore(habit.getStartDate())) {
				newHabit.setStartDate(habit.getStartDate());
			}
			else {
				return new ResponseEntity<String>("Please select a proper date!",HttpStatus.BAD_REQUEST);
 
			}
			newHabit.setName(habit.getName());
			newHabit.setGoalDays(habit.getGoalDays());
			newHabit.setEndDate(habit.getEndDate());
			newHabit.setEnable(true);
			newHabit.setUser(user);
			newHabit.setDescription(habit.getDescription());
			this.habitRepository.save(newHabit);
			return new ResponseEntity<String>("Congrats! You started a new habit! Good luck!", HttpStatus.OK);				
	}
	@GetMapping("/{id}/complete")
    public ResponseEntity<String> markHabitCompleted(@PathVariable Long id) {
		LocalDate currentDate = LocalDate.now();
		System.out.println(currentDate);
        return new ResponseEntity<String>(habitService.markHabitCompleted(id, currentDate),HttpStatus.OK);
    }

	@DeleteMapping("/deleteHabit")
	public ResponseEntity<?> deleteHabit(@RequestBody String habitName){
		if(this.habitRepository.existsByName(habitName)) {
			this.habitRepository.delete(this.habitRepository.getByName(habitName));
			return new ResponseEntity<String>("Habit was deleted successfully!", HttpStatus.OK);
		}
		return new ResponseEntity<String>("Habit does not exist!", HttpStatus.BAD_REQUEST);
	}
	
}
