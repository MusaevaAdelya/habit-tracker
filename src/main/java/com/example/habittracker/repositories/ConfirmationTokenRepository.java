package com.example.habittracker.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.habittracker.entities.ConfirmationToken;

import java.util.Optional;

@Repository
public interface ConfirmationTokenRepository extends CrudRepository<ConfirmationToken, String>{
	
	ConfirmationToken findByConfirmationToken(String confirmToken);

}
