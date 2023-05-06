package com.example.habittracker.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.habittracker.entities.Userr;

@Repository
public interface UserRepository extends JpaRepository<Userr, Long>{

	Optional<Userr> findByEmail(String email);
	Boolean existsByEmail(String email);
	Userr getByEmail(String email);


	
}
