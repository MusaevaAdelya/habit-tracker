package com.example.habittracker.controllers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Optional;
import java.util.UUID;

import com.example.habittracker.dto.request.LoginDTO;
import com.example.habittracker.dto.request.RegisterDTO;
import com.example.habittracker.dto.response.AuthResponseDTO;
import com.example.habittracker.entities.PasswordResetToken;
import com.example.habittracker.services.AuthenticationService;
import com.example.habittracker.services.UserService;
import com.example.habittracker.utils.EmailUtility;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import com.example.habittracker.entities.ConfirmationToken;
import com.example.habittracker.entities.User;
import com.example.habittracker.repositories.ConfirmationTokenRepository;
import com.example.habittracker.repositories.UserRepository;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
	
	private final ConfirmationTokenRepository confirmationTokenRepository;
	private final UserRepository userRepository;
	private final UserService userService;
	private final AuthenticationService authService;
	private final JavaMailSender javaMailSender;

	@PostMapping("/register")
	public ResponseEntity<String> register(@RequestBody RegisterDTO registerDto, final HttpServletRequest servletRequest) throws MessagingException, UnsupportedEncodingException {
		if(userRepository.existsByEmail(registerDto.getEmail())){
			return ResponseEntity.badRequest().body("This email address is already used");
		}

		ConfirmationToken confirmationToken=authService.register(registerDto);

		EmailUtility.sendVerificationEmail(confirmationToken,javaMailSender,applicationUrl(servletRequest));

		return ResponseEntity.ok().body("User registered successfully!"
				+ "We have sent an email with a confirmation link to your email address. In order to complete the sign-up process, please click the confirmation link.\r\n"
				+ "\r\n"
				+ "If you do not receive a confirmation email, please check your spam folder. Also, please verify that you entered a valid email address in our sign-up form.\r\n"
				+ "\r\n"
				+ "If you need assistance, please contact us");
	}

	@PostMapping("/login")
	public ResponseEntity<AuthResponseDTO> authenticate(
			@RequestBody LoginDTO loginRequest
	) {
		return ResponseEntity.ok(authService.authenticate(loginRequest));
	}

	@PostMapping("/refresh-token")
	public ResponseEntity<Void> refreshToken(
			HttpServletRequest request,
			HttpServletResponse response
	) throws IOException {
		authService.refreshToken(request, response);
		return ResponseEntity.ok().build();
	}

	
	@GetMapping(value = "/confirm-account/{confirmationToken}")
	public ResponseEntity<String> confirmUserAccount(@PathVariable String confirmationToken){
		ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);
		if(token != null) {
			User user = token.getUser();
			user.setEnable(true);
			userRepository.save(user);
			return new ResponseEntity<>("Account verified successfully",HttpStatus.OK);
		}
		return new ResponseEntity<>("The link is invalid or broken!", HttpStatus.BAD_REQUEST);
	}

	@PostMapping("/password-reset-request")
	public ResponseEntity<String> resetPasswordRequest(@RequestParam(name="email")String email,
									   final HttpServletRequest servletRequest)
			throws MessagingException, UnsupportedEncodingException {

		Optional<User> user = userService.findByEmail(email);
		if (user.isPresent()) {
			PasswordResetToken passwordResetToken=userService.createPasswordResetTokenForUser(user.get(), UUID.randomUUID().toString());
			EmailUtility.sendPasswordResetCode(passwordResetToken,javaMailSender, applicationUrl(servletRequest));
		}
		return ResponseEntity.ok().body("We have sent a password reset link to "+email+". Click the link within 10 minutes or you'll need to request a new one. If you don't see the email, check your spam folder");
	}

	@PostMapping("/reset-password")
	public ResponseEntity<String> resetPassword(@RequestParam(name="new_password") String newPassword,
								@RequestParam("token") String token){
		boolean tokenVerificationResult = userService.validatePasswordResetToken(token);
		if (!tokenVerificationResult) {
			return ResponseEntity.badRequest().body("Invalid token password reset token");
		}
		Optional<User> theUser = Optional.ofNullable(userService.findUserByPasswordToken(token));
		if (theUser.isPresent()) {
			userService.resetPassword(theUser.get(), newPassword);
			return ResponseEntity.ok().body("Password has been reset successfully");
		}
		return ResponseEntity.badRequest().body("Invalid token password reset token");
	}

	private String applicationUrl(HttpServletRequest request) {
		return "http://"+request.getServerName()+":"
				+request.getServerPort()+request.getContextPath();
	}
	
	
}
