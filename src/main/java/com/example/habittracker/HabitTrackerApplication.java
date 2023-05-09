package com.example.habittracker;

import java.util.Locale;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HabitTrackerApplication{

    public static void main(String[] args) {
		Locale.setDefault(Locale.US);

        SpringApplication.run(HabitTrackerApplication.class, args);
    }

}
