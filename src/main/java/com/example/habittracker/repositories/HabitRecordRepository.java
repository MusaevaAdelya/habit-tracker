package com.example.habittracker.repositories;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.habittracker.entities.HabitRecord;

@Repository
public interface HabitRecordRepository extends JpaRepository<HabitRecord, Integer>{

	HabitRecord getByDate(LocalDate date);
	void deleteAllByHabitId(int habitId);
}
