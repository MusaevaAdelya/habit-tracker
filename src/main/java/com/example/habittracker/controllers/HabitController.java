package com.example.habittracker.controllers;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.habittracker.dto.EnableDto;
import com.example.habittracker.entities.Habit;
import com.example.habittracker.entities.Userr;
import com.example.habittracker.repositories.HabitRepository;
import com.example.habittracker.repositories.UserRepository;

@RestController
@RequestMapping("/main")
public class HabitController {

	@Autowired
	private HabitRepository habitRepository;
	@Autowired
	private UserRepository userRepository;
	
	@PostMapping("/addHabit")
	public ResponseEntity<?> addHabit(@RequestBody String habitName){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Userr user = this.userRepository.getByEmail(auth.getName());
		Habit habit = new Habit(habitName,user);
		this.habitRepository.save(habit);

		return new ResponseEntity<String>("Your personal habit is saved!",HttpStatus.OK);
				
	}
	@SuppressWarnings("deprecation")
	@PostMapping("/enable")
	public ResponseEntity<?> enableHabit(@RequestBody EnableDto enableDto){
		if(this.habitRepository.existsByName(enableDto.getHabitName())) {
	        Date currentDate = new Date();
			Date unProperDate = new Date(2030,5,8);
			
			Habit habit = this.habitRepository.getByName(enableDto.getHabitName());
			habit.setPeriodicity(enableDto.getPeriodicity());
			
			if(currentDate.before(enableDto.getStartDate())) {
				habit.setStartDate(enableDto.getStartDate());
			}
			else {
				return new ResponseEntity<String>("Please select a proper date!",HttpStatus.BAD_REQUEST);
 
			}
			habit.setEndDate(enableDto.getEndDate());
			habit.setGoal(enableDto.getGoal());
			habit.setEnable(true);

			this.habitRepository.save(habit);
			return new ResponseEntity<String>("Congrats! You started a new habit! Good luck!", HttpStatus.OK);
		}
		return new ResponseEntity<String>("Create a habit!",HttpStatus.BAD_REQUEST);
		
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
