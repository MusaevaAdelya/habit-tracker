package com.example.habittracker.controllers;

import com.example.habittracker.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.habittracker.entities.User;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
	
	
	@PostMapping("/add")
	public User add(@RequestBody User user) {
		return userService.add(user);
	}
}
