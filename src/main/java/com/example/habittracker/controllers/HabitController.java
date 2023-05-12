package com.example.habittracker.controllers;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import com.example.habittracker.dto.request.HabitDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.web.bind.annotation.*;

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

import jakarta.mail.MessagingException;

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
	public ResponseEntity<?> addHabit(@RequestBody HabitDTO habit) throws MessagingException{
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = this.userRepository.getByEmail(auth.getName());
		LocalDate currentDate = LocalDate.now();
 
		if(this.habitRepository.existsByName(habit.getName())) {
			Habit newHabit = habitRepository.getByName(habit.getName());
			newHabit.setDescription(habit.getDescription());
			newHabit.setGoalDays(habit.getGoalDays());
			newHabit.setPerDay(habit.getPerDay());
			newHabit.setStartDate(currentDate);
			newHabit.setEndDate(currentDate.plusDays(habit.getGoalDays()));
			newHabit.setEnable(true);
			habitService.save(newHabit);
			
			habitService.sendReminderEmail(newHabit.getId());
			return new ResponseEntity<String>("Habit updated! Good luck!",HttpStatus.OK);
			
		}
		Habit newHabit = new Habit(habit.getName(),habit.getDescription(),habit.getGoalDays(),
				habit.getPerDay(),currentDate,currentDate.plusDays(habit.getGoalDays()),user,true);
		
		habitService.save(newHabit);
		habitService.sendReminderEmail(newHabit.getId());
		return new ResponseEntity<String>("Habit created! Good luck!",HttpStatus.OK);
	}
	@GetMapping("/{id}/complete")
    public ResponseEntity<String> markHabitCompleted(@PathVariable Long id) {
		LocalDate currentDate = LocalDate.now();
        return new ResponseEntity<String>(habitService.markHabitCompleted(id, currentDate),HttpStatus.OK);
    }

	@DeleteMapping("/deleteHabit")
	public ResponseEntity<?> deleteHabit(@RequestBody String habitName){
		if(this.habitRepository.existsByName(habitName)) {
			this.habitService.delete(this.habitService.getByName(habitName));
			return new ResponseEntity<String>("Habit was deleted successfully!", HttpStatus.OK);
		}
		return new ResponseEntity<String>("Habit does not exist!", HttpStatus.BAD_REQUEST);
	}
	
}
