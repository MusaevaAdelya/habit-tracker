package com.example.habittracker.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.habittracker.entities.Userr;

public interface UserRepository extends JpaRepository<Userr, Long>{

	Optional<Userr> findByEmail(String email);
	Boolean existsByEmail(String email);
	
}
