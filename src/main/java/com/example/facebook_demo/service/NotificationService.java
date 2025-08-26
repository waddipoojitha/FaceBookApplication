package com.example.facebook_demo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.facebook_demo.DTO.NotificationDTO;
import com.example.facebook_demo.config.SecurityUtils;
import com.example.facebook_demo.entity.Notification;
import com.example.facebook_demo.entity.User;
import com.example.facebook_demo.exception.ResourceNotFoundException;
import com.example.facebook_demo.repository.NotificationRepository;
import com.example.facebook_demo.repository.UserRepository;

@Service
public class NotificationService {
    @Autowired private NotificationRepository notificationRepo;
    @Autowired private UserRepository userRepo;
    @Autowired private ModelMapper modelMapper;

    public void createNotification(User sender,User receiver,String type,String message){
        Notification notification=new Notification();
        notification.setSender(sender);
        notification.setReceiver(receiver);
        notification.setType(type);
        notification.setMessage(message);
        notification.setCreatedAt(LocalDateTime.now());
        notificationRepo.save(notification);
    }

    public List<NotificationDTO> getNotifications() {
        String username=SecurityUtils.getCurrentUsername();
        User receiver=userRepo.findByUsernameAndDeletedAtIsNull(username).orElseThrow(()->new ResourceNotFoundException("User not found"));
        List<Notification> notifications=notificationRepo.findByReceiverId(receiver.getId());
        return notifications.stream().map(notification->modelMapper.map(notification,NotificationDTO.class)).collect(Collectors.toList());
    }
}