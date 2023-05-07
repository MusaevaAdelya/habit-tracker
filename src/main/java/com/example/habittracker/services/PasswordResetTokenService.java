package com.example.habittracker.services;

import com.example.habittracker.entities.PasswordResetToken;
import com.example.habittracker.entities.Userr;

import java.util.Optional;

public interface PasswordResetTokenService {
    void createPasswordResetTokenForUser(Userr user, String passwordToken);
    String validatePasswordResetToken(String passwordResetToken);
    Optional<Userr> findUserByPasswordToken(String passwordResetToken);
    PasswordResetToken findPasswordResetToken(String token);

}
