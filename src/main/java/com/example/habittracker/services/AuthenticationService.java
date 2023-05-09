package com.example.habittracker.services;

import com.example.habittracker.dto.request.LoginDTO;
import com.example.habittracker.dto.request.RegisterDTO;
import com.example.habittracker.dto.response.AuthResponseDTO;
import com.example.habittracker.entities.AccessToken;
import com.example.habittracker.entities.ConfirmationToken;
import com.example.habittracker.entities.User;
import com.example.habittracker.enums.Role;
import com.example.habittracker.enums.TokenType;
import com.example.habittracker.exceptions.NotFoundException;
import com.example.habittracker.repositories.AccessTokenRepository;
import com.example.habittracker.repositories.ConfirmationTokenRepository;
import com.example.habittracker.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final AccessTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final UserService userService;

    public ConfirmationToken register(RegisterDTO request) {
        User user = User.builder()
                .firstname(request.getFirstName())
                .lastname(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        User savedUser = repository.save(user);

        ConfirmationToken confirmationToken = new ConfirmationToken(savedUser, UUID.randomUUID().toString());
        return confirmationTokenRepository.save(confirmationToken);
    }

    public AuthResponseDTO authenticate(LoginDTO request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> new NotFoundException("User with email " + request.getEmail() + " not found"));
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        userService.revokeAllUserTokens(user);
        userService.saveUserToken(user, jwtToken);
        return AuthResponseDTO.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

//    public void handleOAuth2UserLogin(HttpServletResponse response, OAuth2User oAuth2User) throws IOException {
//        String email=oAuth2User.getAttribute("email");
//        if(!repository.existsByEmail(email)){
//            User newUser=User.builder()
//                    .firstname(oAuth2User.getAttribute("given_name"))
//                    .lastname(oAuth2User.getAttribute("family_name"))
//                    .email(email)
//                    .profileUrl(oAuth2User.getAttribute("picture"))
//                    .enable(true)
//                    .build();
//            repository.save(newUser);
//        }
//
//        User user=repository.findByEmail(email).orElseThrow();
//
//        var jwtToken = jwtService.generateToken(user);
//        var refreshToken = jwtService.generateRefreshToken(user);
//        userService.revokeAllUserTokens(user);
//        userService.saveUserToken(user, jwtToken);
//        var authResponse = AuthResponseDTO.builder()
//                .accessToken(jwtToken)
//                .refreshToken(refreshToken)
//                .build();
//        new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
//
//
//    }

    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail != null) {
            var user = this.repository.findByEmail(userEmail)
                    .orElseThrow(() -> new NotFoundException("User with email " + userEmail + " not found"));
            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                userService.revokeAllUserTokens(user);
                userService.saveUserToken(user, accessToken);
                var authResponse = AuthResponseDTO.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }



}
