package com.example.habittracker.dto.request;

import lombok.Data;

@Data
public class PasswordResetRequest {
    private String email;
    private String oldPassword;
    private String newPassword;
}
