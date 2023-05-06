package com.example.habittracker.controllers;

import java.util.Collections;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.habittracker.config.JwtAuthEntryPoint;
import com.example.habittracker.config.SecurityConfig;
import com.example.habittracker.config.TokenProvider;
import com.example.habittracker.dto.AuthResponseDTO;
import com.example.habittracker.dto.LoginDTO;
import com.example.habittracker.dto.RegisterDTO;
import com.example.habittracker.entities.ConfirmationToken;
import com.example.habittracker.entities.Role;
import com.example.habittracker.entities.Userr;
import com.example.habittracker.repositories.ConfirmationTokenRepository;
import com.example.habittracker.repositories.RoleRepository;
import com.example.habittracker.repositories.UserRepository;
import com.example.habittracker.services.EmailService;

import jakarta.websocket.server.PathParam;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	private ConfirmationTokenRepository confirmationTokenRepository;
	private AuthenticationManager authenticationManager;
	private UserRepository userRepository;
	private RoleRepository roleRepository;
	private PasswordEncoder passwordEncoder;
	private TokenProvider tokenProvider;
	@Autowired
	private EmailService emailService;
	
	

	public AuthController(ConfirmationTokenRepository confirmationTokenRepository,
			AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository,
			PasswordEncoder passwordEncoder, TokenProvider tokenProvider) {
		super();
		this.confirmationTokenRepository = confirmationTokenRepository;
		this.authenticationManager = authenticationManager;
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
		this.tokenProvider = tokenProvider;
	}

	 @DeleteMapping("/{id}")
	    public ResponseEntity<?> delete(@PathVariable Long id) {
		 	
	        userRepository.deleteById(id);
	        return new ResponseEntity<>(HttpStatus.OK);
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
		
		int leftLimit = 97; // letter 'a'
	    int rightLimit = 122; // letter 'z'
	    int targetStringLength = 10;
	    Random random = new Random();
	    StringBuilder buffer = new StringBuilder(targetStringLength);
	    for (int i = 0; i < targetStringLength; i++) {
	        int randomLimitedInt = leftLimit + (int) 
	          (random.nextFloat() * (rightLimit - leftLimit + 1));
	        buffer.append((char) randomLimitedInt);
	    }
	    String generatedString = buffer.toString();
		
		
		
		ConfirmationToken confirmationToken = new ConfirmationToken(user,generatedString);
		this.confirmationTokenRepository.save(confirmationToken);
		
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(user.getEmail());
		mailMessage.setSubject("Please complete registration");
		
		
		
		System.out.println(confirmationToken.getConfirmationToken());
		mailMessage.setText("To confirm your account, please click here : " +
		"http://localhost:8080/auth/confirm-account/"+confirmationToken.getConfirmationToken());
		
		emailService.sendEmail(mailMessage);
		return new ResponseEntity<>("User registered successfully!"
				+ "We have sent an email with a confirmation link to your email address. In order to complete the sign-up process, please click the confirmation link.\r\n"
				+ "\r\n"
				+ "If you do not receive a confirmation email, please check your spam folder. Also, please verify that you entered a valid email address in our sign-up form.\r\n"
				+ "\r\n"
				+ "If you need assistance, please contact us", HttpStatus.OK);
		
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO){
		if(!userRepository.existsByEmail(loginDTO.getEmail())) {
			 return new ResponseEntity<>("Please register", HttpStatus.BAD_REQUEST);
		}
		
		Userr user = userRepository.getByEmail(loginDTO.getEmail());
		
		Authentication authentication = this.authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginDTO.getEmail(),
						loginDTO.getPassword()));
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String token = tokenProvider.generateToken(authentication);
		if(user.isEnable()) {
			
			System.out.println("token in login: " + token);
			return new ResponseEntity<>(new AuthResponseDTO(token), HttpStatus.OK);
		}
		token = "Please confirm your account";
		return new ResponseEntity<>(new AuthResponseDTO(token), HttpStatus.BAD_REQUEST);
	}
	
	@GetMapping(value = "/confirm-account/{confirmationToken}")
	public ResponseEntity<String> confirmUserAccount(@PathVariable String confirmationToken){
		ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);
		System.out.println(token.getConfirmationToken());
		if(token != null) {
			Userr user = token.getUser();
			System.out.println(user.getEmail());
			user.setEnable(true);
			userRepository.save(user);
			System.out.println("success");
			return new ResponseEntity<>("Account verified successfully",HttpStatus.OK);
		}
		return new ResponseEntity<>("The link is invalid or broken!", HttpStatus.BAD_REQUEST);
	}
	
	
}
