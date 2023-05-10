package com.example.habittracker.services;

import com.example.habittracker.dto.request.UpdateUsernameRequest;
import com.example.habittracker.dto.response.ProfileDto;
import com.example.habittracker.entities.AccessToken;
import com.example.habittracker.entities.PasswordResetToken;
import com.example.habittracker.enums.TokenType;
import com.example.habittracker.exceptions.NotFoundException;
import com.example.habittracker.repositories.AccessTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.habittracker.entities.User;
import com.example.habittracker.repositories.UserRepository;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService  {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final PasswordResetTokenService passwordResetTokenService;
	private final AccessTokenRepository tokenRepository;
	private final ImageUploadService imageUploadService;

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

	public void saveUserToken(User user, String jwtToken) {
		var token = AccessToken.builder()
				.user(user)
				.token(jwtToken)
				.tokenType(TokenType.BEARER)
				.expired(false)
				.revoked(false)
				.build();
		tokenRepository.save(token);
	}

	public void revokeAllUserTokens(User user) {
		var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
		if (validUserTokens.isEmpty())
			return;
		validUserTokens.forEach(token -> {
			token.setExpired(true);
			token.setRevoked(true);
		});
		tokenRepository.saveAll(validUserTokens);

	}

	public String saveProfilePicture(User user, MultipartFile multipartFile) {
		String profileUrl=imageUploadService.saveImage(multipartFile);
		user.setProfileUrl(profileUrl);
		userRepository.save(user);
		return "Saved profile image url: "+profileUrl;
	}

	public String updateUserName(String email, UpdateUsernameRequest request) {
		User user=findUserByEmail(email);
		user.setFirstname(request.getFirstName());
		user.setLastname(request.getLastName());
		userRepository.save(user);
		return "Updated user's first name and last name with id "+user.getId();
	}

	public ProfileDto getProfileInfo(String email) {
		User user=findUserByEmail(email);
		return ProfileDto.builder()
				.firstName(user.getFirstname())
				.lastName(user.getLastname())
				.email(user.getEmail())
				.profileUrl(user.getProfileUrl())
				.points(user.getPoints())
				.build();
	}

	public Long deleteUser(String email) {
		User user=findUserByEmail(email);
		user.setEnable(false);
		userRepository.save(user);
		return user.getId();
	}

	private User findUserByEmail(String email){
		return userRepository.findByEmail(email)
				.orElseThrow(()->new NotFoundException("User with email "+email+" not found"));
	}
}


