package com.example.habittracker.services;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.habittracker.entities.Habit;
import com.example.habittracker.entities.HabitRecord;
import com.example.habittracker.repositories.HabitRecordRepository;
import com.example.habittracker.repositories.HabitRepository;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class HabitService {

	@Autowired
	private HabitRepository habitRepository;
	@Autowired
	private HabitRecordRepository habitRecordRepository;
	@Autowired
    private JavaMailSender mailSender;
	
	public Boolean existsByName(String name) {
		return this.habitRepository.existsByName(name);
	}

	public Habit getByName(String name) {
		return this.getByName(name);
	}

	public void save(Habit habit) {
		this.habitRepository.save(habit);
	}

	public void delete(Habit byName) {
		this.habitRepository.delete(byName);

	}

	public String markHabitCompleted(Long habitId, LocalDate completionDate) {
		 Habit habit = habitRepository.findById(habitId).orElseThrow(() -> new RuntimeException("Habit not found"));
		 HabitRecord habitRecord = habitRecordRepository.getByDate(completionDate);
			
			  if (completionDate.isBefore(habit.getStartDate()) ||
			  completionDate.isAfter(habit.getEndDate())) { 
				  return "Completion date is outside habit timeframe";
			  }
			  
			  
			 
			  if ( habitRecord != null) { 
				 
			  return "Habit already completed for date " + completionDate;
			  }
			  
			  
			  int completedDays = habit.getCompletedDays()+1;
			
			  
			  if (completedDays >= habit.getGoalDays()) {
				  return "Goal days exceeded"; }
			  HabitRecord hRecord = new HabitRecord(completionDate,habit);
			  habitRecordRepository.save(hRecord);
			  
			  habit.setCompletedDays(completedDays);
			  habitRepository.save(habit);
			 
	        
	        
		return "Keep going!";
	}
	public void deleteAllFromHabitRecord(int habitId) {
		this.habitRecordRepository.deleteAllByHabitId(habitId);
	}
	public void deleteHabit(String habitName) {
		Habit habit = this.habitRepository.getByName(habitName);
		int habitId = habit.getId();
		deleteAllFromHabitRecord(habitId);
		this.habitRepository.delete(habit);
	
	}
	
	
	@Scheduled(cron = "0 0 9-18 * * *")
    public void sendReminderEmail(int habitId) throws MessagingException {
        	Habit habit = habitRepository.getById(habitId);
        	if(habit != null) {
        		int timesPerDay = habit.getPerDay();
                
                LocalTime currentTime = LocalTime.now();
                if (timesPerDay == 1 && currentTime.isBefore(LocalTime.NOON)) {
                    return; // only send one reminder per day, and don't send before noon
                }

                LocalDate today = LocalDate.now();
                String subject = "Habit reminder: " + habit.getName();
                String message = "Don't forget to work on your habit \"" + habit.getName() + "\" today!";

                for (int i = 0; i < timesPerDay; i++) {
                    LocalDateTime reminderDateTime = LocalDateTime.of(today, LocalTime.of(9 + i, 0)); // send reminders at 9am, 10am, 11am, etc.
                    if (currentTime.isBefore(reminderDateTime.toLocalTime())) {
                        // only send reminders for times later than the current time
                        MimeMessage mimeMessage = mailSender.createMimeMessage();
                        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
                        helper.setTo(habit.getUser().getEmail());
                        helper.setSubject(subject);
                        helper.setText(message, true);
                        helper.setSentDate(Date.from(reminderDateTime.atZone(ZoneId.systemDefault()).toInstant()));
                        mailSender.send(mimeMessage);
                    }
                }
        	}
       
            
        }
    }

