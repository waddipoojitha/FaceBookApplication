package com.example.facebook_demo.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.facebook_demo.entity.User;
import com.example.facebook_demo.repository.UserRepository;

@Service
public class ScheduledTasks {

    @Autowired private SendEmailService sendEmailService;
    @Autowired private UserRepository userRepository;

    @Scheduled(cron = "0 0 6 * * *")
    public void sendBirthdayWishes() {
        LocalDate today = LocalDate.now();

        List<User> users = userRepository.findByDeletedAtIsNull();
        for (User user : users) {
            LocalDate dob = user.getDateOfBirth();
            if (dob != null && dob.getDayOfMonth() == today.getDayOfMonth() && dob.getMonthValue() == today.getMonthValue()) {
                System.out.println("Happy Birthday, " + user.getFirstName());
                sendEmailService.sendEmail(user.getEmail(), "Happy birthday " + user.getFirstName() + " " + user.getLastName(), "Birthday wish");
            }
        }
    }
    @Scheduled(cron="0 0 0 * * *")
    public void deleteSoftDeletedUsers(){
        LocalDate today=LocalDate.now();
        List<User> deletedUsers=userRepository.findByDeletedAtIsNotNull();

        for(User user:deletedUsers){
            LocalDate deleteDate=user.getDeletedAt().toLocalDate();
            long daysBetween = ChronoUnit.DAYS.between(deleteDate, today);
            
            if(daysBetween>=30){
                userRepository.delete(user);
            }
        }
    }

}