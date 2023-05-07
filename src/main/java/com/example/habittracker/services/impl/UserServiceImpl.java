package com.example.habittracker.services.impl;

import com.example.habittracker.services.PasswordResetTokenService;
import com.example.habittracker.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.habittracker.entities.Userr;
import com.example.habittracker.repositories.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final PasswordResetTokenService passwordResetTokenService;

	@Override
	public List<Userr> getUsers() {
		return userRepository.findAll();
	}

	@Override
	public Userr add(Userr user) {
		return this.userRepository.save(user);
	}


	@Override
	public Optional<Userr> findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public void resetPassword(Userr theUser, String newPassword) {
		theUser.setPassword(passwordEncoder.encode(newPassword));
		userRepository.save(theUser);
	}

	@Override
	public String validatePasswordResetToken(String token) {
		return passwordResetTokenService.validatePasswordResetToken(token);
	}

	@Override
	public Userr findUserByPasswordToken(String token) {
		return passwordResetTokenService.findUserByPasswordToken(token).get();
	}

	@Override
	public void createPasswordResetTokenForUser(Userr user, String passwordResetToken) {
		passwordResetTokenService.createPasswordResetTokenForUser(user, passwordResetToken);
	}


}
