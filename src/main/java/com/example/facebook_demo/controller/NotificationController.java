package com.example.facebook_demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.facebook_demo.DTO.NotificationDTO;
import com.example.facebook_demo.response.APIResponse;
import com.example.facebook_demo.service.NotificationService;

@RestController
@RequestMapping("notifications")
public class NotificationController {

    @Autowired private NotificationService notificationService;
    @GetMapping
    public ResponseEntity<APIResponse<List<NotificationDTO>>> getNotifications(){
        List<NotificationDTO> notifications=notificationService.getNotifications();
        APIResponse<List<NotificationDTO>> apiResponse = new APIResponse<>(" All notifications fetched", notifications);
        return new ResponseEntity<>(apiResponse,HttpStatus.OK);
    }
}
