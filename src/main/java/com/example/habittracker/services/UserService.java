package com.example.habittracker.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.habittracker.entities.Userr;
import com.example.habittracker.repositories.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	
	public Userr add(Userr user) {
		return this.userRepository.save(user);
	}
}
