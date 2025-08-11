package com.example.facebook_demo.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.facebook_demo.entity.User;
import com.example.facebook_demo.repository.UserRepository;

@Service
public class ScheduledTasks {

    private final SendEmailService sendEmailService;
    @Autowired private UserRepository userRepository;

    ScheduledTasks(SendEmailService sendEmailService) {
        this.sendEmailService = sendEmailService;
    }
    //@Scheduled(cron = "0 0 0 * * ?") // Every day at midnight
    @Scheduled(cron = "0 0 6 * * *")
    public void sendBirthdayWishes() {
        LocalDate today = LocalDate.now();

        List<User> users = userRepository.findAll();
        for (User user : users) {
            LocalDate dob = user.getDateOfBirth();
            if (dob != null && dob.getDayOfMonth() == today.getDayOfMonth() && dob.getMonthValue() == today.getMonthValue()) {
                System.out.println("Happy Birthday, " + user.getFirstName());
                sendEmailService.sendEmail(user.getEmail(), "Happy birthday " + user.getFirstName() + " " + user.getLastName(), "Birthday wish");
            }
        }
    }
}