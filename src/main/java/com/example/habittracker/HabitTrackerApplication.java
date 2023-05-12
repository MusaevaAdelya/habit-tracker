package com.example.habittracker;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.habittracker.entities.Habit;
import com.example.habittracker.enums.GoalPeriodType;
import com.example.habittracker.enums.HabitType;
import com.example.habittracker.repositories.HabitRepository;

@SpringBootApplication
public class HabitTrackerApplication implements ApplicationRunner{

	@Autowired
	HabitRepository habitRepository;
    public static void main(String[] args) {
		Locale.setDefault(Locale.US);

        SpringApplication.run(HabitTrackerApplication.class, args);
    }

	@Override
	public void run(ApplicationArguments args) throws Exception {
			
		System.out.println("start");

	}

}
