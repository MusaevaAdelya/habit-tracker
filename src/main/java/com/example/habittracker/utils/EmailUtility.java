package com.example.habittracker.utils;

import com.example.habittracker.entities.ConfirmationToken;
import com.example.habittracker.entities.PasswordResetToken;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.experimental.UtilityClass;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import java.io.UnsupportedEncodingException;

@UtilityClass
public class EmailUtility {

    public void sendVerificationEmail( ConfirmationToken confirmationToken, JavaMailSender mailSender, String applicationUrl)
            throws MessagingException, UnsupportedEncodingException {
        String toAddress = confirmationToken.getUser().getEmail();
        String fromAddress = "msvadelya@gmail.com";
        String senderName = "Habit Tracker Service";
        String subject = "Please complete registration";
        String content = "Dear [[name]],<br>"
                + "To confirm your account, please click here :<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                + "Thank you,<br>"
                + "Habit Tracker Service.";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);

        content = content.replace("[[name]]", confirmationToken.getUser().getFirstname());
        String verifyURL = applicationUrl + "/api/v1/auth/confirm-account/" + confirmationToken.getConfirmationToken();
        content = content.replace("[[URL]]", verifyURL);

        helper.setText(content, true);
        mailSender.send(message);

        System.out.println(content);

    }

    public static void sendPasswordResetCode(PasswordResetToken passwordResetToken, JavaMailSender mailSender, String applicationUrl) throws MessagingException, UnsupportedEncodingException {
        String toAddress = passwordResetToken.getUser().getEmail();
        String fromAddress = "msvadelya@gmail.com";
        String senderName = "Habit Tracker Service";
        String subject = "Password Reset";
        String content = "Dear [[name]],<br>"
                + "You recently requested to reset your password<br>"
                + "Please, follow the link below to complete the action.<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">RESET PASSWORD</a></h3>"
                + "Thank you,<br>"
                + "Habit Tracker Service.";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);

        content = content.replace("[[name]]", passwordResetToken.getUser().getFirstname());

        String resetURL = applicationUrl + "/api/v1/auth/reset-password?token=" + passwordResetToken.getToken();
        content = content.replace("[[URL]]", resetURL);

        helper.setText(content, true);
        mailSender.send(message);

        System.out.println(content);
    }

}