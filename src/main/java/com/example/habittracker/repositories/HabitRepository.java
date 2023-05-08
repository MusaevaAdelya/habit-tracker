package com.example.habittracker.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.habittracker.entities.Habit;

@Repository
public interface HabitRepository extends JpaRepository<Habit, Integer>{

	Boolean existsByName(String name);
	Habit getByName(String name);
}
