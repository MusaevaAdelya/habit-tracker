package com.example.habittracker.services;

import com.example.habittracker.entities.PasswordResetToken;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.habittracker.entities.User;
import com.example.habittracker.repositories.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService  {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final PasswordResetTokenService passwordResetTokenService;

	public List<User> getUsers() {
		return userRepository.findAll();
	}

	public User add(User user) {
		return userRepository.save(user);
	}

	public Optional<User> findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	public void resetPassword(User theUser, String newPassword) {
		theUser.setPassword(passwordEncoder.encode(newPassword));
		userRepository.save(theUser);
	}

	public boolean validatePasswordResetToken(String token) {
		return passwordResetTokenService.validatePasswordResetToken(token);
	}

	public User findUserByPasswordToken(String token) {
		return passwordResetTokenService.findUserByPasswordToken(token).get();
	}

	public PasswordResetToken createPasswordResetTokenForUser(User user, String passwordResetToken) {
		return passwordResetTokenService.createPasswordResetTokenForUser(user, passwordResetToken);
	}


}
