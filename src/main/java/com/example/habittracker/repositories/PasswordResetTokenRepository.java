package com.example.habittracker.repositories;

import com.example.habittracker.entities.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    PasswordResetToken findByToken(String passwordResetToken);

    @Transactional
    @Modifying(clearAutomatically=true, flushAutomatically=true)
    void deleteByUserId(Long userId);
}
