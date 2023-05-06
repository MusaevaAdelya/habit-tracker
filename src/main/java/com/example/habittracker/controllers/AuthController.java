package com.example.habittracker.controllers;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.habittracker.config.JwtAuthEntryPoint;
import com.example.habittracker.config.SecurityConfig;
import com.example.habittracker.config.TokenProvider;
import com.example.habittracker.dto.AuthResponseDTO;
import com.example.habittracker.dto.LoginDTO;
import com.example.habittracker.dto.RegisterDTO;
import com.example.habittracker.entities.Role;
import com.example.habittracker.entities.Userr;
import com.example.habittracker.repositories.RoleRepository;
import com.example.habittracker.repositories.UserRepository;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	private AuthenticationManager authenticationManager;
	private UserRepository userRepository;
	private RoleRepository roleRepository;
	private PasswordEncoder passwordEncoder;
	private TokenProvider tokenProvider;
	
	
	@Autowired
	public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository,
			RoleRepository roleRepository, PasswordEncoder passwordEncoder, TokenProvider tokenProvider) {
		super();
		this.authenticationManager = authenticationManager;
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
		this.tokenProvider = tokenProvider;
	}

	@PostMapping("/register")
	public ResponseEntity<String> register(@RequestBody RegisterDTO registerDto){
		if(this.userRepository.existsByEmail(registerDto.getEmail())){
			return new ResponseEntity<String>("This email address is already used", HttpStatus.BAD_REQUEST);
		}
		
		Userr user = new Userr();
		user.setEmail(registerDto.getEmail());
		user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
		
		
		
		Role role = this.roleRepository.findByName("USER").get();
		user.setRoles(Collections.singletonList(role));
		
		this.userRepository.save(user);
		
		return new ResponseEntity<>("User registered successfully!", HttpStatus.OK);
		
	}
	
	@PostMapping("/login")
	public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginDTO loginDTO){
		Authentication authentication = this.authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginDTO.getEmail(),
						loginDTO.getPassword()));
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String token = tokenProvider.generateToken(authentication);
		System.out.println("token in login: " + token);
		return new ResponseEntity<>(new AuthResponseDTO(token), HttpStatus.OK);
	}
	
	
	
}
