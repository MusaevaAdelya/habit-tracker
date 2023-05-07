package com.example.habittracker.controllers;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import com.example.habittracker.dto.request.PasswordResetRequest;
import com.example.habittracker.services.UserService;
import com.example.habittracker.services.impl.UserServiceImpl;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.example.habittracker.config.TokenProvider;
import com.example.habittracker.dto.response.AuthResponseDTO;
import com.example.habittracker.dto.request.LoginDTO;
import com.example.habittracker.dto.request.RegisterDTO;
import com.example.habittracker.entities.ConfirmationToken;
import com.example.habittracker.entities.Role;
import com.example.habittracker.entities.Userr;
import com.example.habittracker.repositories.ConfirmationTokenRepository;
import com.example.habittracker.repositories.RoleRepository;
import com.example.habittracker.repositories.UserRepository;
import com.example.habittracker.services.EmailService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
	
	private final ConfirmationTokenRepository confirmationTokenRepository;
	private final AuthenticationManager authenticationManager;
	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;
	private final TokenProvider tokenProvider;
	private final EmailService emailService;
	private final UserService userServiceImpl;

	 @DeleteMapping("/{id}")
	    public ResponseEntity<?> delete(@PathVariable Long id) {
		 	
	        userRepository.deleteById(id);
	        return new ResponseEntity<>(HttpStatus.OK);
	    }
	
	@PostMapping("/register")
	public ResponseEntity<String> register(@RequestBody RegisterDTO registerDto, final HttpServletRequest request){
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
		String url=applicationUrl(request);
		mailMessage.setText("To confirm your account, please click here : " + url +"/api/v1/auth/confirm-account/"+confirmationToken.getConfirmationToken());
		
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

	@PostMapping("/password-reset-request")
	public ResponseEntity<String> resetPasswordRequest(@RequestParam(name="email")String email,
									   final HttpServletRequest servletRequest)
			throws MessagingException, UnsupportedEncodingException {

		Optional<Userr> user = userServiceImpl.findByEmail(email);
		if (user.isPresent()) {
			String passwordResetToken = UUID.randomUUID().toString();
			userServiceImpl.createPasswordResetTokenForUser(user.get(), passwordResetToken);
			String url = applicationUrl(servletRequest)+"/api/v1/auth/reset-password?token="+passwordResetToken;
			emailService.sendPasswordResetVerificationEmail(email,url);
		}
		return ResponseEntity.ok().body("We have sent a password reset link to "+email+". Click the link within 10 minutes or you'll need to request a new one. If you don't see the email, check your spam folder");
	}

	@PostMapping("/reset-password")
	public ResponseEntity<String> resetPassword(@RequestParam(name="new_password") String newPassword,
								@RequestParam("token") String token){
		String tokenVerificationResult = userServiceImpl.validatePasswordResetToken(token);
		if (!tokenVerificationResult.equalsIgnoreCase("valid")) {
			return ResponseEntity.badRequest().body("Invalid token password reset token");
		}
		Optional<Userr> theUser = Optional.ofNullable(userServiceImpl.findUserByPasswordToken(token));
		if (theUser.isPresent()) {
			userServiceImpl.resetPassword(theUser.get(), newPassword);
			return ResponseEntity.ok().body("Password has been reset successfully");
		}
		return ResponseEntity.badRequest().body("Invalid token password reset token");
	}

	public String applicationUrl(HttpServletRequest request) {
		return "http://"+request.getServerName()+":"
				+request.getServerPort()+request.getContextPath();
	}
	
	
}
