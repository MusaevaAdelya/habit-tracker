package com.example.habittracker.controllers;

import com.example.habittracker.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.habittracker.entities.Userr;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userServiceImpl;
	
	
	@PostMapping("/add")
	public Userr add(@RequestBody Userr user) {
		return this.userServiceImpl.add(user);
	}
}
