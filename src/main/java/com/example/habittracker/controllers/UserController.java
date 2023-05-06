package com.example.habittracker.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.habittracker.entities.Userr;
import com.example.habittracker.services.UserService;

@RestController
public class UserController {

	@Autowired
	UserService userService;
	
	
	@PostMapping("/add")
	public Userr add(@RequestBody Userr user) {
		return this.userService.add(user);
	}
}
