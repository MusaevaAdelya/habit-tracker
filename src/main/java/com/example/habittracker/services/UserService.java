package com.example.habittracker.services;

import com.example.habittracker.entities.Userr;

import java.util.List;
import java.util.Optional;


public interface UserService {
    List<Userr> getUsers();

    Userr add(Userr user);

    Optional<Userr> findByEmail(String email);

    void resetPassword(Userr theUser, String newPassword);

    String validatePasswordResetToken(String token);

   Userr findUserByPasswordToken(String token);

    void createPasswordResetTokenForUser(Userr user, String passwordResetToken);
}
