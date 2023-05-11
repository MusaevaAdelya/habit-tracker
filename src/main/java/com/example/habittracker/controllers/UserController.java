package com.example.habittracker.controllers;

import com.example.habittracker.dto.request.UpdateUsernameRequest;
import com.example.habittracker.dto.response.ProfileDto;
import com.example.habittracker.entities.User;
import com.example.habittracker.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;

	@PostMapping("/save-profile-picture")
	public ResponseEntity<String> saveProfilePicture(Authentication authentication, @RequestPart MultipartFile multipartFile) throws Exception{
		return ResponseEntity.ok(userService.saveProfilePicture((User) authentication,multipartFile));
	}

	@PutMapping("/update-name")
	public ResponseEntity<String> updateUsersFirstAndLastName(Authentication authentication,
															  @RequestBody UpdateUsernameRequest newUsername) throws Exception{
		log.info(authentication.getName());
		return ResponseEntity.ok(userService.updateUserName(authentication.getName(),newUsername));
	}

	@GetMapping("/get-profile-info")
	public ResponseEntity<ProfileDto> getProfileInfo(Authentication authentication){
		return ResponseEntity.ok(userService.getProfileInfo(authentication.getName()));
	}

	@DeleteMapping("/delete")
	public ResponseEntity<String> deleteUser(Authentication authentication){
		return ResponseEntity.ok("Deleted user with id: "+userService.deleteUser(authentication.getName()));
	}



}
